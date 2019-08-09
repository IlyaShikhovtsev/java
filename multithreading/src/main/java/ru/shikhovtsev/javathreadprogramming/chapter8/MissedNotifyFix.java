package ru.shikhovtsev.javathreadprogramming.chapter8;

public class MissedNotifyFix {
  private Object proceedLock;
  private boolean okToProceed;

  public MissedNotifyFix() {
    print("in MissedNotify()");
    proceedLock = new Object();
    okToProceed = false;
  }

  public void waitToProceed() throws InterruptedException {
    print("in waitToProceed() - entered");

    synchronized (proceedLock) {
      while (!okToProceed) {
        print("in waitToProceed() - about to wait()");
        proceedLock.wait();
        print("in waitToProceed() - back from wait()");
      }
    }

    print("in waitToProceed() - leaving");
  }

  public void proceed() {
    print("in proceed() - entered");

    synchronized (proceedLock) {
      print("in proceed() - about to notifyAll()");
      okToProceed = true;
      print("changed okToProceed to true");
      proceedLock.notifyAll();
      print("in proceed() - back from notifyAll()");
    }

    print("in proceed() - leaving");
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }

  public static void main(String[] args) {
    final MissedNotifyFix mn = new MissedNotifyFix();

    Runnable runA = () -> {
      try {
        Thread.sleep(1000);
        mn.waitToProceed();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };

    Thread threadA = new Thread(runA, "threadA");
    threadA.start();

    Runnable runB = () -> {
      try {
        Thread.sleep(500);
        mn.proceed();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };

    Thread threadB = new Thread(runB, "threadB");
    threadB.start();

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {}

    print("about to invoke interrupt() on threadA");
    threadA.interrupt();
  }
}
