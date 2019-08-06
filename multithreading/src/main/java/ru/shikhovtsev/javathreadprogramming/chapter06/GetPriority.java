package ru.shikhovtsev.javathreadprogramming.chapter06;

public class GetPriority {
  private static Runnable makeRunnable() {
    return () -> {
      for (int i = 0; i < 5; i++) {
        Thread t = Thread.currentThread();
        System.out.println("in run(), prior=" + t.getPriority() + ", name=" + t.getName());

        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
      }
    };
  }

  public static void main(String[] args) {
    System.out.println("in main() - Thread.currenctThread.getPrior=" + Thread.currentThread().getPriority());
    System.out.println("in main() - Thread.currenctThread.getName=" + Thread.currentThread().getName());

    Thread threadA = new Thread(makeRunnable(), "threadA");
    threadA.start();

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
    }

    System.out.println("in main() - threadA.getPrior=" + threadA.getPriority());
  }
}
