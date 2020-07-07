package runnables;

class MyJob2 implements Runnable {
  volatile long count = 0;

  @Override
  public void run() {
    for (int i = 0; i < 10_000; i++) {
      count++;
    }
  }
}
public class ReadModifyWrite {
  public static void main(String[] args) throws Throwable {
    MyJob2 mj = new MyJob2();
    Thread t1 = new Thread(mj);
    Thread t2 = new Thread(mj);
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println("Count is " + mj.count);
  }
}
