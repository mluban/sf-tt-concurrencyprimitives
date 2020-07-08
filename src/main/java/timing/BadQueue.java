package timing;

import java.util.Arrays;

public class BadQueue<E> {
  private E[] data = (E[]) (new Object[10]);
  private int count = 0;

  public void put(E e) throws InterruptedException {
    synchronized (this) {
      // MUST be WHILE, not if, you might wake for wrong reasons...
      while (count >= 10) {
        this.wait(); // deschedules CPU (until...) AND releases the lock
        // DO NOT WAIT unless data are transationcally "safe"
        // wait DOES NOT CONTINUE until lock is regained.
      }
      data[count++] = e;
      this.notify();
    }
  }

  public E take() throws InterruptedException {
    synchronized (this) {
      while (count <= 0) {
        this.wait();
      }
//      this.notify(); // "Safe", but silly
      E result = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      this.notify(); // logical location :)
      return result;
    }
  }

  public static void main(String[] args) {
    BadQueue<int[]> queue = new BadQueue<>();
    new Thread(() -> {
      System.out.println("Producer starting...");
      for (int i = 0; i < 1_000; i++) {
        try {
          int[] data = {-1, i};
          if (i < 100) {
            Thread.sleep(1);
          }
          data[0] = i;
          if (i == 500) {
            data[1] = -99;
          }
          queue.put(data); data = null;
        } catch (InterruptedException ie) {
          System.out.println("Ouch, that shouldn't happen!");
        }
      }
      System.out.println("Producer finishing...");
    }).start();

    new Thread(() -> {
      System.out.println("Consumer starting...");
      for (int i = 0; i < 1_000; i++) {
        try {
          int [] data = queue.take();
          if (data[0] != data[1] || data[0] != i) {
            System.out.println("***** ERROR at "
                + i + " values " + Arrays.toString(data));
          }
          if (i > 900) {
            Thread.sleep(1);
          }
        } catch (InterruptedException ie) {
          System.out.println("Ouch, that hurt the consumer...");
        }
      }
      System.out.println("Consumer finishing...");
    }).start();
    System.out.println("Workers started...");
  }
}
