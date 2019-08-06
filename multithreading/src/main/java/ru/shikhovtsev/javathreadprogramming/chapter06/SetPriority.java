package ru.shikhovtsev.javathreadprogramming.chapter06;

public class SetPriority {
  private static Runnable makeRunnable() {
    return () -> {
      for (int i = 0; i < 5; i++) {
        Thread t = Thread.currentThread();

        System.out.println("in run() - prior" + t.getPriority() + ", name="+ t.getName());

        try {
          Thread.sleep(2000);
        } catch (InterruptedException e){

        }
      }
    };
  }

  public static void main(String[] args) {
    Thread threadA = new Thread(makeRunnable(), "threadA");
    threadA.setPriority(8);
    threadA.start();

    Thread threadB = new Thread(makeRunnable(), "threadB");
    threadB.setPriority(2);
    threadB.start();

    Runnable r = () -> {
      Thread threadC = new Thread(makeRunnable(), "threacC");
      threadC.start();
    };

    Thread threadD = new Thread(r, "threadD");
    threadD.setPriority(7);
    threadD.start();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {

    }

    threadA.setPriority(3);
    System.out.println("in main() - threadA.getPriority()=" + threadA.getPriority());
  }
}
