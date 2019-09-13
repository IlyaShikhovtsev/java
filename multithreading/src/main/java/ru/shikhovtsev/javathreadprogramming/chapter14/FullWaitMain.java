package ru.shikhovtsev.javathreadprogramming.chapter14;

public class FullWaitMain {
  private FullWait fullWait;
  private Thread internalThread;
  private volatile boolean noStopRequested;

  public FullWaitMain(FullWait fw) {
    fullWait = fw;

    noStopRequested = true;
    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    internalThread = new Thread(r);
    internalThread.start();
  }

  private void runWork() {
    int count = 6;

    while (noStopRequested) {
      fullWait.setValue(count);
      System.out.println("just set value to " + count);
      count++;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void stopRequest() {
    noStopRequested = false;
    internalThread.interrupt();
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }

  public static void waitFor (FullWait fw, int val, long limit) throws  InterruptedException {
    System.out.println("about to waitUntilAtLeast(" + val + ", " + limit + ") .... )");

    long startTime = System.currentTimeMillis();
    boolean retVal = fw.waitUntilAtLeast(val, limit);
    long endTime = System.currentTimeMillis();

    System.out.println("waited for " + (endTime - startTime) + " ms, retVal=" + retVal + "\n-----------------");
  }

  public static void main(String[] args) {
    try {
      FullWait fw = new FullWait(5);
      FullWaitMain fwm = new FullWaitMain(fw);

      Thread.sleep(500);

      waitFor(fw, 10, 1000L);
      waitFor(fw, 6, 5000L);
      waitFor(fw, 6, -1000L);
      waitFor(fw, 15, -1000L);
      waitFor(fw, 999, 5000L);
      waitFor(fw, 20, 0L);

      fwm.stopRequest();
    } catch (InterruptedException e) {
      System.err.println("*unexpectedly* interrupted somewhere in main()");
    }
  }
}
