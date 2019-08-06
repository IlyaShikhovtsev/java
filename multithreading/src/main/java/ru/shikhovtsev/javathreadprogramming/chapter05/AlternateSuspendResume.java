package ru.shikhovtsev.javathreadprogramming.chapter05;

public class AlternateSuspendResume implements Runnable {
  private volatile int firstVal;
  private volatile int secondVal;
  private volatile boolean suspended;

  public boolean areValuesEqual() {
    return firstVal == secondVal;
  }

  public void run() {
    try {
      suspended = false;
      firstVal = 0;
      secondVal = 0;
      workMethod();
    } catch (InterruptedException x) {
      System.out.println("interrupted while in workMethod()");
    }
  }

  private void workMethod() throws InterruptedException {
    int val = 1;

    while (true) {
      waitWhileSuspended();
      stepOne(val);
      stepTwo(val);
      val++;

      waitWhileSuspended();

      Thread.sleep(200);
    }
  }

  private void stepOne(int newVal) throws InterruptedException {
    firstVal = newVal;
    Thread.sleep(300);
  }

  private void stepTwo(int newVal) {
    secondVal = newVal;
  }

  public void suspendRequest() {
    suspended = true;
  }

  public void resumeRequest() {
    suspended = false;
  }

  private void waitWhileSuspended() throws InterruptedException {
    while (suspended) {
      Thread.sleep(200);
    }
  }

  public static void main(String[] args) {
    var dsr = new AlternateSuspendResume();
    Thread t = new Thread(dsr);
    t.start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {

    }
    for (int i = 0; i < 10; i++) {
      dsr.suspendRequest();
      try {
        Thread.sleep(350);
      } catch (InterruptedException e) {

      }
      System.out.println("dsr.areValEqual() " + dsr.areValuesEqual());
      dsr.resumeRequest();
      try {
        Thread.sleep((long) (Math.random() * 2000.0));
      } catch (InterruptedException e) {

      }
    }
  }

}
