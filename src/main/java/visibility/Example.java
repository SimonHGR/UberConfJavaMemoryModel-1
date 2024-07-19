package visibility;

public class Example {
  private static /*volatile*/ boolean stop = false;

  public static void main(String[] args) throws Throwable {
    Runnable worker = () -> {
      System.out.println("Worker starting");
      while (!stop)
        System.out.println("stop is ");;
      System.out.println("Worker stopping");
    };

    Thread t1 = new Thread(worker);
    t1.start();
    System.out.println("Worker started by main");
    Thread.sleep(1000);
    stop = true;
    System.out.println("main, stop is " + stop);
    System.out.println("main exiting");
  }
}
