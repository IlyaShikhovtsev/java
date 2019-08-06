package ru.shikhovtsev.javathreadprogramming.chapter05;

import ru.shikhovtsev.javathreadprogramming.chapter17.BooleanLock;

public class BestReplacement {
  private Thread internalThread;
  private volatile boolean stopRequested;
  private BooleanLock suspendRequested;
  private BooleanLock internalThreadSuspended;

  public BestReplacement() {
    stopRequested = false;

    suspendRequested = new BooleanLock(false);
    internalThreadSuspended = new BooleanLock(false);

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
    int count = 0;

    while (!stopRequested) {
      try {
        waitWhileSuspended();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        continue;
      }

      System.out.println("Part I - count = " + count);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      System.out.println("Part II - count = " + count);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      System.out.println("Part III - count " + count);
      count++;
    }
  }

  private void waitWhileSuspended() throws InterruptedException {
    synchronized (suspendRequested) {
      if (suspendRequested.isTrue()) {
        try {
          internalThreadSuspended.setValue(true);
          suspendRequested.waitUntilFalse(0);
        } finally {
          internalThreadSuspended.setValue(false);
        }
      }
    }
  }

  public void suspendRequest() {
    suspendRequested.setValue(true);
  }

  public void resumeRequest() {
    suspendRequested.setValue(false);
  }

  public boolean waitForActualSuspension(long msTimeout) throws InterruptedException {
    return true;
  }

  public void stopRequest() {
    stopRequested = true;
    internalThread.interrupt();
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }

  public static void main(String[] args) {
    try {
      var br = new BestReplacement();
      System.out.println("---> Just created, br.isAlive() = " + br.isAlive());
      Thread.sleep(4200);

      long startTime = System.currentTimeMillis();
      br.suspendRequest();
      System.out.println("--> just submitted a suspendRequest");

      boolean suspensionTookEffect = br.waitForActualSuspension(10000);
      long stopTime = System.currentTimeMillis();

      if (suspensionTookEffect) {
        System.out.println("->> the internall thread tool " + (stopTime - startTime) + " ms to notice");
      } else {
        System.out.println("->> the internal thread did not notice");
      }

      Thread.sleep(5000);

      br.resumeRequest();
      System.out.println("just submitted a resumeRequest");
      Thread.sleep(2200);

      br.stopRequest();
      System.out.println("->> just submitted a stopRequest");
    } catch (InterruptedException e) {

    }
  }
}
