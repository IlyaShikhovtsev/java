package ru.shikhovtsev.javathreadprogramming.chapter8.cubbyhole;

public class CubbyHole {
  private Object slot;

  public CubbyHole() {
    slot = null;
  }

  public synchronized  void putIn(Object obj) throws InterruptedException {
    print("in putIn() - entering");

    while (slot != null) {
      print("in putIn() - occupied, about to wait()");
      wait();
      print("in putIn*() - notified, back from wait()");
    }

    slot = obj;
    print("in putIn() - filled slot, about to notifyAll()");
    notifyAll();
    print("in putIn() - leaving");
  }

  public synchronized Object takeOut() throws InterruptedException {
    print("takeOut() -entering");

    while (slot == null) {
      print("in takeOut() - empty, about to wait()");
      wait();
      print("in takeOut() - notified, back from wait()");
    }

    Object obj = slot;
    slot = null;
    print("in takeOut() - emptied slot, about to notifyAll()");
    notifyAll();

    return obj;
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }
}
