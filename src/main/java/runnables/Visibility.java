package runnables;

public class Visibility {
  static volatile boolean stop = false;

  public static void main(String[] args) throws Throwable {
    new Thread(()-> {
      System.out.println("Worker started...");
      while(!stop)
//        System.out.print(".");
        ;
      System.out.println("Worker shutting down...");
    }).start();

    Thread.sleep(1000);
    stop = true;
    System.out.println("Stop set to " + stop + " main exiting...");
  }
}
