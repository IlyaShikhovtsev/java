package ru.shikhovtsev.javathreadprogramming.chapter14;

public class FullWait {
  private volatile int value;

  public FullWait(int initialValue) {
    value = initialValue;
  }

  public synchronized void setValue(int newValue) {
    if (value != newValue) {
      value = newValue;
      notifyAll();
    }
  }

  public synchronized boolean waitUntilAtLeast(int minValue, long msTimeout) throws InterruptedException {
    if (msTimeout == 0L) {
      while (value < minValue) {
        wait();
      }

      return true;
    }

    long endTime = System.currentTimeMillis() + msTimeout;
    long msRemaining = msTimeout;

    while ((value < minValue) && (msRemaining > 0L)) {
      wait(msRemaining);
      msRemaining = endTime - System.currentTimeMillis();
    }

    return (value >= minValue);
  }

  public String toString() {
    return getClass().getName() + "[value=" + value + "]";
  }

}
