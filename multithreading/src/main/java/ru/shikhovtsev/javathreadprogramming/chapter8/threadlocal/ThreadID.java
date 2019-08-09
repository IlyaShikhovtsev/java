package ru.shikhovtsev.javathreadprogramming.chapter8.threadlocal;

public class ThreadID extends ThreadLocal<Integer> {
  private int nextID;

  public ThreadID() {
    nextID = 10001;
  }

  private synchronized Integer getNewID() {
    Integer id = nextID;
    nextID++;
    return id;
  }

  protected Integer initialValue() {
    print("in initialValue()");
    return getNewID();
  }

  public int getThreadID() {
    return get();
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }
}
