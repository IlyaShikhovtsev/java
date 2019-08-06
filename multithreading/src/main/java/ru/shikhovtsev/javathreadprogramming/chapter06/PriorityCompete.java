package ru.shikhovtsev.javathreadprogramming.chapter06;

public class PriorityCompete {
  private volatile int count;
  private boolean yield;
  private Thread internalThread;
  private volatile boolean noStopRequested;

  public PriorityCompete(String name, int priorit, boolean yield) {
    count = 0;
    this.yield = yield;
    noStopRequested = true;
    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    internalThread = new Thread(r, name);
    internalThread.setPriority(priorit);
  }

  private void runWork() {
    Thread.yield();

    while(noStopRequested) {
      if(yield) {
        Thread.yield();
      }

      count++;

      for (int i = 0; i < 1000; i++) {
        double x = i * Math.PI / Math.E;
      }
    }
  }

  public void startRequest() {
    internalThread.start();
  }

  public void stopRequest() {
    noStopRequested = false;
  }

  public int getCount() {
    return count;
  }

  public String getNameAndPriority() {
    return internalThread.getName() + ": prior=" + internalThread.getPriority();
  }

  public static void runSet(boolean yield) {
    var pc = new PriorityCompete[3];
    pc[0] = new PriorityCompete("PC0", 3, yield);
    pc[1] = new PriorityCompete("PC1", 6, yield);
    pc[2] = new PriorityCompete("PC2", 6, yield);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e){}

    for (int i = 0; i < pc.length; i++) {
      pc[i].startRequest();
    }

    long startTime = System.currentTimeMillis();
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {}

    for (int i = 0; i < pc.length; i++) {
      pc[i].stopRequest();
    }

    long stopTime = System.currentTimeMillis();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {}

    int totalCount = 0;

    for (int i = 0; i < pc.length; i++) {
      totalCount += pc[i].getCount();
    }

    System.out.println("totalCount=" + totalCount + ", count/ms=" + roundTo(((double) totalCount) / (stopTime- startTime), 3));

    for (int i = 0; i < pc.length; i++) {
      double pers = roundTo(100.0 * pc[i].getCount() / totalCount, 2);
      System.out.println(pc[i].getNameAndPriority() + ", " + pers + "%, count=" + pc[i].getCount());
    }
  }

  public static double roundTo(double val, int places) {
    double factor = Math.pow(10, places);
    return ((int) ((val* factor) + 0.5)) / factor;
  }

  public static void main(String[] args) {
    Runnable r = () -> {
      System.out.println("Run without using yield");
      System.out.println("=======================");
      runSet(false);

      System.out.println();
      System.out.println("Run using yield()");
      System.out.println("=======================");
      runSet(true);
    };

    Thread t = new Thread(r, "PriorityCompete");
    t.setPriority(Thread.MAX_PRIORITY - 1);
    t.start();
  }
}
