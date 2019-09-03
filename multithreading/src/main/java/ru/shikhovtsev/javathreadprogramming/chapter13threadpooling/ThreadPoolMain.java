package ru.shikhovtsev.javathreadprogramming.chapter13threadpooling;

public class ThreadPoolMain {

  public static Runnable makeRunnable(final String name, final long firstDelay) {
    return new Runnable() {
      @Override
      public String toString() {
        return name;
      }

      @Override
      public void run() {
        try {
          System.out.println(name + ": starting up");
          Thread.sleep(firstDelay);
          System.out.println(name + ": doing some stuff");
          Thread.sleep(2000);
          System.out.println(name + ": leaving");
        } catch (InterruptedException e) {
          System.out.println(name + ": got interrupted!");
          return;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
  }

  public static void main(String[] args) {
    try {
      ThreadPool pool = new ThreadPool(3);

      Runnable ra = makeRunnable("RA", 3000);
      pool.execute(ra);

      Runnable rb = makeRunnable("RB", 1000);
      pool.execute(rb);

      Runnable rc = makeRunnable("RC", 2000);
      pool.execute(rc);

      Runnable rd = makeRunnable("RD", 60000);
      pool.execute(rd);

      Runnable re = makeRunnable("RE", 1000);
      pool.execute(re);

      pool.stopRequestIdleWorkers();
      Thread.sleep(2000);
      pool.stopRequestIdleWorkers();

      Thread.sleep(5000);
      pool.stopRequestAllWorkers();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
