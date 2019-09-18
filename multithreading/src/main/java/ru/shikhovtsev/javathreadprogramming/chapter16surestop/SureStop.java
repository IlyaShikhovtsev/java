package ru.shikhovtsev.javathreadprogramming.chapter16surestop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SureStop {
  private static class Entry {
    private Thread thread;
    private long stopTime;

    private Entry(Thread t, long stop) {
      thread = t;
      stopTime = stop;
    }
  }

  private static SureStop ss;

  static {
    ss = new SureStop();
  }

  private List<Entry> stopList;
  private List pendingList;
  private Thread internalThread;

  private SureStop() {
    stopList = new LinkedList<>();

    pendingList = new ArrayList(20);

    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    internalThread = new Thread(r);
    internalThread.setDaemon(true);

    internalThread.setPriority(Thread.MAX_PRIORITY);
    internalThread.start();
  }

  private void runWork() {
    try {
      while (true) {
        Thread.sleep(500);
        long sleepTime = checkStopList();

        synchronized (pendingList) {
          if (pendingList.size() < 1) {
            pendingList.wait(sleepTime);
          }

          if (pendingList.size() > 0) {
            stopList.addAll(pendingList);
            pendingList.clear();
          }
        }
      }
    } catch (InterruptedException e) {

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private long checkStopList() {
    long currTime = System.currentTimeMillis();
    long minTime = Long.MAX_VALUE;

    Iterator<Entry> iter = stopList.iterator();

    while (iter.hasNext()) {
      Entry entry = iter.next();

      if (entry.thread.isAlive()) {
        if (entry.stopTime < currTime) {
          try {
            entry.thread.stop();
          } catch (SecurityException e) {
            System.err.println("SureStop was not permitted to stop thread = " + entry.thread);
            e.printStackTrace();
          }

          iter.remove();
        } else {
          minTime = Math.min(entry.stopTime, minTime);
        }
      } else {
        iter.remove();
      }
    }

    long sleepTime = minTime - System.currentTimeMillis();

    sleepTime = Math.max(50, sleepTime);

    return sleepTime;
  }
}
