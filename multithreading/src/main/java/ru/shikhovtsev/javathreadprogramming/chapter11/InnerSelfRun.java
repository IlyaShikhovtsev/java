package ru.shikhovtsev.javathreadprogramming.chapter11;

public class InnerSelfRun {
  private Thread internalThread;
  private volatile boolean noStopRequested;

  public InnerSelfRun() {
    System.out.println("in constructor - initializing...");

    noStopRequested = true;

    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception e) {e.printStackTrace();}
    };

    internalThread = new Thread(r);
    internalThread.start();
  }

  private void runWork() {
    while (noStopRequested) {
      System.out.println("in runWork() - still going...");
    }

    try {
      Thread.sleep(700);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void stopRequest() {
    noStopRequested = false;
    internalThread.interrupt();
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }
}
