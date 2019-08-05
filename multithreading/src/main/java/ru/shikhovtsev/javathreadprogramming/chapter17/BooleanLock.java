package ru.shikhovtsev.javathreadprogramming.chapter17;

public class BooleanLock {
  private boolean value;

  public BooleanLock(boolean initialValue) {
    value = initialValue;
  }

  public BooleanLock() {
    this(false);
  }

  public synchronized void setValue(boolean newValue) {
    if (newValue != value) {
      value = newValue;
      notifyAll();
    }
  }

  public synchronized boolean waitToSetTrue(long msTimeout) throws InterruptedException {
    boolean success = waitUntilFalse(msTimeout);
    if (success) {
      setValue(true);
    }
    return success;
  }

  public synchronized boolean waitToSetFalse(long msTimeout) throws InterruptedException {
    boolean success = waitUntilTrue(msTimeout);
    if (success) {
      setValue(false);
    }
    return success;
  }

  public synchronized boolean isTrue() {
    return value;
  }

  public synchronized boolean isFalse() {
    return !value;
  }

  public synchronized boolean waitUntilTrue(long msTimeout) throws InterruptedException {
    return waitUntilStateIs(true, msTimeout);
  }

  public synchronized boolean waitUntilFalse(long msTimeout) throws InterruptedException {
    return waitUntilStateIs(false, msTimeout);
  }

  public synchronized boolean waitUntilStateIs(boolean state, long msTimeout) throws InterruptedException {
    if (msTimeout == 0L) {
      while (value != state) {
        wait();
      }

      return true;
    }

    long endTime = System.currentTimeMillis() + msTimeout;
    long msRemaining = msTimeout;

    while ((value != state) && (msRemaining > 0L)) {
      wait(msRemaining);
      msRemaining = endTime - System.currentTimeMillis();
    }

    return (value == state);
  }

}
