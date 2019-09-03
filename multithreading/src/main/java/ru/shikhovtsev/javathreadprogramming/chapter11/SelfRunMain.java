package ru.shikhovtsev.javathreadprogramming.chapter11;

public class SelfRunMain {
  public static void main(String[] args) {
    var sr = new SelfRun();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {}

    sr.stopRequest();
  }
}
