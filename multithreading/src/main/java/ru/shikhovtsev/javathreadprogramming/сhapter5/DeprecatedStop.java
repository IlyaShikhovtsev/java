package ru.shikhovtsev.javathreadprogramming.сhapter5;

public class DeprecatedStop implements Runnable {

  public void run() {
    int count = 0;

    while(true) {
      System.out.println("Running ... count = " + count);
      count++;

      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {

      }
    }
  }

  public static void main(String[] args) {
    var ds = new DeprecatedStop();
    Thread t = new Thread(ds);
    t.start();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e ) {

    }

    t.stop();
  }

}
