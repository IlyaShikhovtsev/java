package ru.shikhovtsev.javathreadprogramming.chapter11;

public class SelfRun implements Runnable {
  private Thread internalThread;
  private volatile boolean noStopRequested;

  public SelfRun() {
    System.out.println("in constructor - initializing");

    noStopRequested = true;
    internalThread = new Thread(this);
    internalThread.start();
  }

  public void run() {
    if (Thread.currentThread() != internalThread) {
      throw new RuntimeException("only the internal thread is allowed to invoke run()");
    }

    while (noStopRequested) {
      System.out.println("in run() - still going...");

      try {
        Thread.sleep(700);
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

  public static void main(String[] args) {
    new SelfRun();
  }

}
