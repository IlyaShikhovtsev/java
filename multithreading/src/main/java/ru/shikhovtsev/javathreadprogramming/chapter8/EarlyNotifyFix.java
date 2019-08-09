package ru.shikhovtsev.javathreadprogramming.chapter8;

import java.security.cert.CollectionCertStoreParameters;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class EarlyNotifyFix {
  private List<String> list;

  public EarlyNotifyFix() {
    list = Collections.synchronizedList(new LinkedList<>());
  }

  public String removeItem() throws InterruptedException {
    print("in removeItem - entering");

    synchronized (list) {
      while (list.isEmpty()) {
        print("in removeItem() - about to wait()");
        list.wait();
        print("in removeItem() - done with wait()");
      }

      String item = list.remove(0);

      print("in removeItem - leaving");
      return item;
    }
  }

  public void addItem(String item) {
    print("in addItem() - entering");
    synchronized (list) {
      list.add(item);
      print("in addItem() - just added: '" + item +"'");

      list.notifyAll();
      print("in addItem() - just notified");
    }
    print("in addItem() - leaving");
  }

  public static void main(String[] args) {
    final EarlyNotifyFix en = new EarlyNotifyFix();
    Runnable runA = () -> {
      try {
        String item  = en.removeItem();
        print("in run() - returned: " + item);
      } catch (InterruptedException ix) {
        print("interrupted");
      } catch (Exception e) {
        print("threw an Exception!!!\n" + e);
      }
    };

    Runnable runB = () -> en.addItem("Hello!");

    try {
      Thread threadA1 = new Thread(runA, "ThreadA1");
      threadA1.start();

      Thread.sleep(500);

      Thread threadA2 = new Thread(runA, "ThreadA2");
      threadA2.start();

      Thread.sleep(500);

      Thread threadB = new Thread(runB, "ThreadB");
      threadB.start();

      Thread.sleep(10000);

      threadA1.interrupt();
      threadA2.interrupt();
    } catch (InterruptedException e) {}
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }
}
