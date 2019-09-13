package ru.shikhovtsev.javathreadprogramming.chapter15breakingoutofblockedIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

public class DefiantStream {
  public static void main(String[] args) {
    final InputStream in = System.in;

    Runnable r = () -> {
      try {
        System.err.println("abour to try to read from in");
        in.read();
        System.err.println("just read from in");
      } catch (InterruptedIOException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        Thread currThread = Thread.currentThread();
        System.err.println("inside finally:\n +   currThread = " + currThread + "\n" + "currThread.isAlive()=" + currThread.isAlive());
      }
    };

    Thread t = new Thread(r);
    t.start();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {

    }

    System.err.println("aboout to interrupt thread");
    t.interrupt();
    System.err.println("just interrupted thread");

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}

    System.err.println("about to stop thread");
    t.stop();
    System.err.println("just stopped thread, t.isAlive() =" + t.isAlive());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {}

    System.err.println("t.isAlive()= " + t.isAlive());
    System.err.println("leaving main");
  }
}
