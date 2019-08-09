package ru.shikhovtsev.javathreadprogramming.chapter8.cubbyhole;

public class CubbyHoleMain {
  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }

  public static void main(String[] args) {
    final var ch = new CubbyHole();

    Runnable runA = () -> {
      try {
        String str;
        Thread.sleep(500);

        str = "multithreaded";
        ch.putIn(str);
        print("in run() - just put in: " + str);

        str = "programming";
        ch.putIn(str);
        print("in run() - just put in: " + str);

        str = "with Java";
        ch.putIn(str);
        print("in run() - just put in: " + str);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };

    Runnable runB = () -> {
      try {
        Object obj;
        obj = ch.takeOut();
        print("in run() - just took out: " + obj);

        Thread.sleep(500);

        obj = ch.takeOut();
        print("in run() - just took out: " + obj);

        obj = ch.takeOut();
        print("in run() - just took out: " + obj);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };

    Thread threadA = new Thread(runA, "threadA");
    threadA.start();

    Thread threadB = new Thread(runB, "threadB");
    threadB.start();
  }
}
