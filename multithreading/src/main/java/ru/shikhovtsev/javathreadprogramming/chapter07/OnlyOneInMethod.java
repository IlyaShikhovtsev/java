package ru.shikhovtsev.javathreadprogramming.chapter07;

public class OnlyOneInMethod {
  private String objID;

  public OnlyOneInMethod(String objID) {
    this.objID = objID;
  }

  public synchronized  void doStuff(int val) {
    print("entering doStuff()");
    int num = val * 2 + objID.length();
    print("in doStuff() - local var num = " + num);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}

    print("leaving doStuff()");
  }

  public void print(String msg) {
    threadPrint("objID=" + objID + " - " + msg);
  }

  public static void threadPrint(String msg) {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + ": " + msg);
  }

  public static void main(String[] args) {
    final OnlyOneInMethod v = new OnlyOneInMethod("obj1");

    Runnable runA = () -> v.doStuff(3);
    Thread threadA = new Thread(runA, "threadA");
    threadA.start();

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {}

    Runnable runB = () -> v.doStuff(7);

    Thread threadB = new Thread(runB, "threadB");
    threadB.start();
  }
}
