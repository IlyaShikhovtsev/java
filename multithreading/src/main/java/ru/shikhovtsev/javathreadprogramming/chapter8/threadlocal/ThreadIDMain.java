package ru.shikhovtsev.javathreadprogramming.chapter8.threadlocal;

public class ThreadIDMain implements Runnable {
  private ThreadID var;

  public ThreadIDMain(ThreadID var) {
    this.var = var;
  }

  public void run() {
    try {
      print("var.getThreadID()=" + var.getThreadID());
      Thread.sleep(2000);
      print("var.getThreadID()=" + var.getThreadID());
    } catch (InterruptedException e) {}
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }

  public static void main(String[] args) {
    var tid = new ThreadID();
    var shared = new ThreadIDMain(tid);

    try {
      Thread threadA = new Thread(shared, "threadA");
      threadA.start();

      Thread.sleep(500);

      Thread threadB = new Thread(shared, "threadB");
      threadB.start();

      Thread.sleep(500);

      Thread threadC = new Thread(shared, "threadC");
      threadC.start();
    } catch (InterruptedException e) {}
  }
}
