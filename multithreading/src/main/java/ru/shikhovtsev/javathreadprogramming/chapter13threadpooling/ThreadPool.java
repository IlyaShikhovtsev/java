package ru.shikhovtsev.javathreadprogramming.chapter13threadpooling;

import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {
  private ObjectFIFO idleWorkers;
  private ThreadPoolWorker[] workerList;

  public ThreadPool(int numberOfThreads) {
    numberOfThreads = Math.max(1, numberOfThreads);
    idleWorkers = new ObjectFIFO(numberOfThreads);
    workerList = new ThreadPoolWorker[numberOfThreads];

    for (int i = 0; i < workerList.length; i++) {
      workerList[i] = new ThreadPoolWorker(idleWorkers);
    }
  }

  public void execute(Runnable target) throws InterruptedException {
    ThreadPoolWorker worker = (ThreadPoolWorker) idleWorkers.remove();
    worker.process(target);
  }

  public void stopRequestIdleWorkers() {
    try {
      Object[] idle = idleWorkers.removeAll();
      for (int i = 0; i < idle.length; i++) {
        ((ThreadPoolWorker) idle[i]).stopRequest();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void stopRequestAllWorkers() {
    stopRequestIdleWorkers();

    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {}

    for (int i = 0; i < workerList.length; i++) {
      if (workerList[i].isAlive()) {
        workerList[i].stopRequest();
      }
    }
  }

  static class ThreadPoolWorker {
    private static int nextWorkerID = 0;

    private ObjectFIFO idleWorkers;
    private int workerID;
    private ObjectFIFO handoffBox;

    private Thread internalThread;
    private volatile boolean noStopRequested;

    public ThreadPoolWorker(ObjectFIFO idleWorkers) {
      this.idleWorkers = idleWorkers;

      workerID = getNextWorkerID();
      handoffBox = new ObjectFIFO(1);

      noStopRequested = true;

      Runnable r = () -> {
        try {
          runWork();
        } catch (Exception e) {
          e.printStackTrace();
        }
      };

      internalThread = new Thread(r);
      internalThread.start();
    }

    public static synchronized int getNextWorkerID() {
      int id = nextWorkerID;
      nextWorkerID++;
      return id;
    }

    public void process(Runnable target) throws InterruptedException {
      handoffBox.add(target);
    }

    private void runWork() {
      while (noStopRequested) {
        try {
          System.out.println("workerID=" + workerID + ", ready for work");

          idleWorkers.add(this);

          Runnable r = (Runnable) handoffBox.remove();

          System.out.println("workerID=" + workerID + ", starting execution of new Runnable: " + r);

          runIt(r);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    }

    private void runIt(Runnable r) {
      try {
        r.run();
      } catch (Exception runex) {
        System.err.println("Uncaught exception fell through from run()");
        runex.printStackTrace();
      } finally {
        Thread.interrupted();
      }
    }

    public void stopRequest() {
      System.out.println("workerID=" + workerID + ", stopRequest() received.");
      noStopRequested = false;
      internalThread.interrupt();
    }

    public boolean inAlive() {
      return internalThread.isAlive();
    }
  }
}
