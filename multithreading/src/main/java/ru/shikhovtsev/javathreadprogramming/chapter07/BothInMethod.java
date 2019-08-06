package ru.shikhovtsev.javathreadprogramming.chapter07;

public class BothInMethod {
  private String objID;

  public BothInMethod(String objID) {
    this.objID = objID;
  }

  public void doStuff(int val) {
    print("entering doStuff()");
    int num = val * 2 + objID.length();
    print("In doStuff() - local variable num = " + num);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
    }

    print("leaving doStuff()");
  }

  public void print(String msg) {
    threadPrint("objId=" + objID + " - " + msg);
  }

  public static void threadPrint(String msg) {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + ": " + msg);
  }

  public static void main(String[] args) {
    final BothInMethod bim = new BothInMethod("obj1");

    Runnable runA = () -> bim.doStuff(3);

    Thread tA = new Thread(runA, "threadA");
    tA.start();

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {}

    Runnable runB = () -> bim.doStuff(7);
    Thread tB = new Thread(runB, "threadB");
    tB.start();
  }
}
