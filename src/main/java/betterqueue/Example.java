package betterqueue;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BetterQueue<E> {
  Lock lock = new ReentrantLock();
  Condition notFull = lock.newCondition();
  Condition notEmpty = lock.newCondition();

  private E[] data = (E[]) new Object[10];
  private int count = 0;

  public void put(E e) throws InterruptedException {
    lock.lock();
    try {
      while (count == 10) {
        notFull.await();
      }
      // valid, buf silly
//      this.rendezvous.notify();
      data[count++] = e;
      // sensible
//      this.rendezvous.notify();
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  public E take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0) {
        notEmpty.await();
      }
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
//      this.rendezvous.notify();
      notFull.signal();

      return rv;
    } finally {
      lock.unlock();
    }
  }
}

public class Example {
  public static void main(String[] args) {
    BetterQueue<int[]> queue = new BetterQueue<>();
    new Thread(() -> {
      System.out.println("Starting producer");
      try {
        for (int i = 0; i < 10_000; i++) {
          int[] data = new int[2];
          data[0] = i; // transactionally dubious!
          data[1] = i; // now stable :)
          // test the test:
          if (i == 5_000) {
            data[0] = -1;
          }
          queue.put(data);
          data = null; // not strictly necessary
          if (i < 100) {
            Thread.sleep(1); // verify empty queue behavior
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("shutdown requested...");
      }
      System.out.println("Producer completed");
    }).start(); // producer
    new Thread(() -> {
      System.out.println("Consumer starting");
      try {
        for (int i = 0; i < 10_000; i++) {
          if (i > 9_900) {
            Thread.sleep(1);
          }
          int[] data = queue.take();
          if (data[0] != i || data[0] != data[1]) {
            System.out.println("***** ERROR at i = " + i);
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("shut= down consumer requested");
      }
      System.out.println("Consumer finished");
    }).start(); // consumer
  }
}
