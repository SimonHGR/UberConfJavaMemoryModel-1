package trivial;

public class Simple {
  public static void main(String[] args) {
    int [] ia = {0};
    Runnable task = () -> {
      for (/*int i = 0*/; ia[0] < 10_000; ia[0]++) {
        System.out.println(Thread.currentThread() + " i = " + ia[0]);
      }
    };
    Thread t1 = new Thread(task);
    Thread t2 = new Thread(task);
    t1.start();
    t2.start();
    System.out.println("main finished");
  }
}
