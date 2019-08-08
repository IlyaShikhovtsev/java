package ru.shikhovtsev.javathreadprogramming.chapter07;

public class FixedWrite {
  private String fname;
  private String lname;

  public synchronized void setNames(String firstName, String lastName) {
    print("entering setNames()");
    fname = firstName;

    if (fname.length() < 5) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {}
    } else {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {}
    }

    lname = lastName;

    print("leaving setNames() - " + lname + ", " + fname);
  }

  public static void print(String msg) {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + ": " + msg);
  }

  public static void main(String[] args) {
    final FixedWrite cw = new FixedWrite();

    Runnable runA = () -> cw.setNames("George", "washington");

    Thread threadA = new Thread(runA, "threadA");
    threadA.start();

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {}

    Runnable runB = () -> cw.setNames("Abe", "Loncoln");

    Thread threadB = new Thread(runB, "threadB");
    threadB.start();
  }

}
