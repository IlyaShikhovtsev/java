package ru.shikhovtsev.javathreadprogramming.chapter11;

public class InnerSelfRunMain {
  public static void main(String[] args) {
    var sr = new InnerSelfRun();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {}

    sr.stopRequest();
  }
}
