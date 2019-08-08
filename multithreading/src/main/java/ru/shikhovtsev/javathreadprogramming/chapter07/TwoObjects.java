package ru.shikhovtsev.javathreadprogramming.chapter07;

public class TwoObjects {
  private String objId;

  public TwoObjects(String objId) {
    this.objId = objId;
  }

  public synchronized void doStuff(int val) {
    print("entering doStuff");
    int num = val * 2 + objId.length();
    print("in doStuff - local variable num=" + num);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}

    print("leaving doStuff()");
  }

  public void print(String msg) {
    threadPrint("objId=" + objId + " - " + msg);
  }

  public static void threadPrint(String msg) {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + ": " + msg);
  }

  public static void main(String[] args) {
    final TwoObjects obj1 = new TwoObjects("obj1");
    final TwoObjects obj2 = new TwoObjects("obj2");

    Runnable runA = () -> obj1.doStuff(3);

    Thread threadA = new Thread(runA, "threadA");
    threadA.start();

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {}

    Runnable runB = () -> obj2.doStuff(7);

    Thread threadB = new Thread(runB, "thread2");
    threadB.start();
  }
}
