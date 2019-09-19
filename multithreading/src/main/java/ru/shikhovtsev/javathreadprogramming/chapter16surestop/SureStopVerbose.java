package ru.shikhovtsev.javathreadprogramming.chapter16surestop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SureStopVerbose {

  private static class Entry {
    private Thread thread;
    private long stopTime;

    private Entry(Thread t, long stop) {
      thread = t;
      stopTime = stop;
    }
  }

  private static SureStopVerbose ss;

  static {
    ss = new SureStopVerbose();
    print("SureStopVerbose instance is created");
  }

  private List<Entry> stopList;
  private List<Entry> pendingList;
  private Thread internalThread;

  private SureStopVerbose() {
    stopList = new LinkedList<>();

    pendingList = new ArrayList<>(20);

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
        print("about to sleep for 0.5 seconds");
        Thread.sleep(500);
        print("done with sleep for 0.5 seconds");

        long sleepTime = checkStopList();
        print("back from checkStopList(), sleepTime=" + sleepTime);

        synchronized (pendingList) {
          if (pendingList.size() < 1) {
            print("about to wait on pendingList for " + sleepTime + " ms");
            long start = System.currentTimeMillis();
            pendingList.wait();
            long elapsedTime = System.currentTimeMillis();

            print("waited on pendingList for " + elapsedTime + " ms");
          }

          if (pendingList.size() > 0) {
            print("copying " + pendingList.size() + " elements from pendingList to stopList");

            int oldSize = stopList.size();
            stopList.addAll(pendingList);
            pendingList.clear();
            int newSize = stopList.size();
            print("pendingList.size()=" + pendingList.size() + ", stopList grew by " + (newSize - oldSize));
          }
        }
      }
    } catch (InterruptedException e) {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private long checkStopList() {
    print("entering checkStop() - stopList.size()=" + stopList.size());

    long currTime = System.currentTimeMillis();
    long minTime = Long.MAX_VALUE;

    Iterator<Entry> iter = stopList.iterator();
    while (iter.hasNext()) {
      Entry entry = iter.next();

      if (entry.thread.isAlive()) {
        print ("thread is alive - " + entry.thread.getName());
        if (entry.stopTime < currTime) {
          print ("timed out, stopping - " + entry.thread.getName());

          try {
            entry.thread.stop();
          } catch (SecurityException e) {
            System.err.println("SureStop was not permitted to stop thread = " + entry.thread);
            e.printStackTrace();
          }

          iter.remove();
        } else {
          minTime = Math.min(entry.stopTime, minTime);
          print("new minTime = " + minTime);
        }
      } else {
        print ("thread died on its own - " + entry.thread.getName());
        iter.remove();
      }
    }

    long sleepTime = minTime - System.currentTimeMillis();

    sleepTime = Math.max(50, sleepTime);

    print("leaving checkStopList() - stopList.size() = " + stopList.size());
    return sleepTime;
  }

  private void addEntry(Entry entry) {
    synchronized (pendingList) {
      pendingList.add(entry);

      pendingList.notify();
      print("added entry to pendingList, name=" + entry.thread.getName() + ", stopTime = " + entry.stopTime + ", in " + (entry.stopTime - System.currentTimeMillis()) +" ms");
    }
  }

  public static void ensureStop (Thread t, long msGracePeriod) {
    print ("entering ensureStop() - name=" + t.getName() + ", msGracePeriod=" + msGracePeriod);

    if (!t.isAlive()) {
      print("already stopped, not added to list = " + t.getName());
      return;
    }

    long stopTime = System.currentTimeMillis() + msGracePeriod;
    Entry entry = new Entry(t, stopTime);
    ss.addEntry(entry);
    print("leaving ensureStop() - name= " + t.getName());
  }

  private static void print(String msg) {
    Thread t = Thread.currentThread();
    String name = t.getName();
    if (t == ss.internalThread) {
      name = "SureStopThread";
    }

    System.out.println(name +": " + msg);
  }
}
