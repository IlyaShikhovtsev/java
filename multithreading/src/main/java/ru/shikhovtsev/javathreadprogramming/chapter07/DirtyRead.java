package ru.shikhovtsev.javathreadprogramming.chapter07;

public class DirtyRead {
  private String fname;
  private String lname;

  public String getNames() {
    return lname + fname;
  }

  public synchronized void setNames(String firstName, String lastName) {
    print("entering setNames()");
    fname = firstName;

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }

    lname = lastName;
    print("leaving setNames() - " + lname + ", " + fname);
  }

  public static void print(String msg) {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + ": " + msg);
  }

  public static void main(String[] args) {
    final DirtyRead cw = new DirtyRead();
    cw.setNames("GEorge", "Washing");
    Runnable runA = () -> cw.setNames("Abe", "LINCli");

    Thread threadA = new Thread(runA, "threadA");
    threadA.start();

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
    }

    Runnable runB = () -> print("getNames() = " + cw.getNames());

    Thread threadB = new Thread(runB, "threadB");
    threadB.start();
  }


}
