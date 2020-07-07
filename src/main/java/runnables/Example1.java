package runnables;

class MyJob implements Runnable {
  int i = 0;
  @Override
  public void run() {
    for (; i < 10; i++) {
      System.out.println(Thread.currentThread().getName() +
          " value of i is " + i);
    }
    System.out.println(Thread.currentThread().getName() + " finished");
  }
}
public class Example1 {
  public static void main(String[] args) {
    MyJob mj = new MyJob();
    Thread t1 = new Thread(mj);
    Thread t2 = new Thread(mj);
    t1.start();
    t2.start();
    System.out.println(Thread.currentThread().getName() + " Ending...");
  }
}
