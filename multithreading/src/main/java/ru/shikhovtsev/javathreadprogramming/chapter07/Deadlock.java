package ru.shikhovtsev.javathreadprogramming.chapter07;

public class Deadlock {
  private String objID;

  public Deadlock(String objID) {
    this.objID = objID;
  }

  public synchronized void checkOther(Deadlock other) {
    print("entering checkOther()");

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}

    print("in checkOther() - about to " + "invoke 'other.action()'");
    other.action();

    print("leaving checkOther");
  }

  public synchronized void action() {
    print("entering action()");

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {}

    print("leaving action");
  }

  public void print(String msg) {
    threadPrint("objID=" + objID + " - " + msg);
  }
  public static void threadPrint(String msg) {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + ": " + msg);
  }

  public static void main(String[] args) {
    final Deadlock obj1 = new Deadlock("obj1");
    final Deadlock obj2 = new Deadlock("obj2");

    Runnable runA = () -> obj1.checkOther(obj2);

    Thread threadA = new Thread(runA, "threadA");
threadA.start();

    Runnable runB = () -> obj2.checkOther(obj1);

    Thread threadB = new Thread(runB, "threadB");
    threadB.start();
  }
}
