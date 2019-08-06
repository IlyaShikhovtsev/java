package ru.shikhovtsev.javathreadprogramming.chapter05.daemon;

public class DeamonThreadMain {
  public static void main(String[] args) {
    System.out.println("entering main()");

    Thread t = new Thread(new DaemonThread());
    t.setDaemon(false);
    t.start();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {

    }

    System.out.println("leaving main()");
  }
}
