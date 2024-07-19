package transaction;

public class Example {
  public static /*volatile*/ long count = 0;

  public static void main(String[] args) throws Throwable {
    Object rendezvous = new Object();
    Runnable counter = () -> {
      for (int i = 0; i < 1_000_000_000; i++) {
        synchronized (rendezvous) {
          count++;
        }
      }
    };
    Thread t1 = new Thread(counter);
    Thread t2 = new Thread(counter);
    long start = System.nanoTime();
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    long duration = System.nanoTime() - start;
    System.out.println(count);
    System.out.printf("time was %7.3f\n", (duration / 1_000_000_000.0));
  }
}
