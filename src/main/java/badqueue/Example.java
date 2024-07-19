package badqueue;

class BadQueue<E> {
  private Object rendezvous = new Object();
  private E[] data = (E[])new Object[10];
  private int count = 0;

  public void put(E e) throws InterruptedException {
    synchronized (this.rendezvous) {
      while (count == 10) {
        this.rendezvous.wait();
      }
      // valid, buf silly
//      this.rendezvous.notify();
      data[count++] = e;
      // sensible
//      this.rendezvous.notify();
      this.rendezvous.notifyAll();
    }
  }

  public E take() throws InterruptedException {
    synchronized (this.rendezvous) {
      while (count == 0) {
        this.rendezvous.wait();
      }
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
//      this.rendezvous.notify();
      this.rendezvous.notifyAll();

      return rv;
    }
  }
}

public class Example {
}
