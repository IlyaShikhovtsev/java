package ru.shikhovtsev.javathreadprogramming.chapter15breakingoutofblockedIO.calc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CalcServer {
  private ServerSocket ss;

  private List workerList;

  private Thread internalThread;

  private volatile boolean noStopRequested;

  public CalcServer(int port) throws IOException {
    ss =new ServerSocket(port);
    workerList = new LinkedList();

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

  private void runWork() {
    System.out.println("In CalcServer - ready to accept connections");

    while (noStopRequested) {
      try {
        System.out.println("In CalcServer - about to block waiting for a new connection");
        Socket sock = ss.accept();
        System.out.println("in CalcServer - received new connection");

        workerList.add(new CalcWorker(sock));
      } catch (IOException e) {
        if (noStopRequested) {
          e.printStackTrace();
        }
      }
    }

    System.out.println("in CalcServer - putting in a stop request to all the workers");
    Iterator<CalcWorkerk> iter = workerList.iterator();
    while (iter.hasNext()) {
      CalcWorker worker = iter.next();
      worker.stopRequest();
    }

    System.out.println("in CalcServer - leaving runWork()");
  }

  public void stopRequest() {
    System.out.println("in CalcServer - entering stopRequest()");
    noStopRequested = false;
    internalThread.interrupt();

    if (ss != null) {
      try {
        ss.close();
      } catch (IOException e) {

      } finally {
        ss = null;
      }
    }
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }

  public static void main(String[] args) {
    int port = 2001;

    try {
      CalcServer server = new CalcServer(port);
      Thread.sleep(15000);
      server.stopRequest();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {}
  }
}
