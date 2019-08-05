package ru.shikhovtsev.javathreadprogramming.сhapter5;

public class AlternateStop implements Runnable {

  private volatile boolean stopRequested;
  private Thread thread;

  public void run() {
    thread = Thread.currentThread();
    stopRequested = false;

    int count = 0;

    while(!stopRequested) {
      System.out.println("Running ... count = " + count);
      count++;

      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void stopRequest() {
    stopRequested = true;

    if( thread != null) {
      thread.interrupt();
    }
  }

  public static void main(String[] args) {
    var ds = new AlternateStop();
    Thread t = new Thread(ds);
    t.start();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e ) {

    }

    ds.stopRequest();
  }

}
