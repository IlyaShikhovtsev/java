package ru.shikhovtsev.javathreadprogramming.chapter05.daemon;

public class DaemonThread implements Runnable {

  public void run() {
    System.out.println("entering run()");

    try {
      System.out.println("in run - currentThread()=" + Thread.currentThread());

      while(true) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        System.out.println("in run - woke up again");
      }
    } finally {
      System.out.println("leaving run()");
    }
  }
}
