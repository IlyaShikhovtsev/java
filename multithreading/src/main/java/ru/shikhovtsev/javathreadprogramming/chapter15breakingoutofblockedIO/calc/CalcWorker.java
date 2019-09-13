package ru.shikhovtsev.javathreadprogramming.chapter15breakingoutofblockedIO.calc;

import java.io.*;
import java.net.Socket;

public class CalcWorker {
  private InputStream sockIn;
  private OutputStream sockOut;
  private DataInputStream dataIn;
  private DataOutputStream dataOut;

  private Thread internalThread;
  private volatile boolean noStopRequested;

  public CalcWorker(Socket sock) throws IOException {
    sockIn = sock.getInputStream();
    sockOut = sock.getOutputStream();

    dataIn = new DataInputStream(new BufferedInputStream(sockIn));
    dataOut = new DataOutputStream(new BufferedOutputStream(sockOut));

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
    while (noStopRequested) {
      try {
        System.out.println("in CalcWorker - about to block waiting to read a double");
        double val = dataIn.readDouble();
        System.out.println("in CalcWorker - read a double!");
        dataOut.writeDouble(Math.sqrt(val));
        dataOut.flush();
      } catch (IOException e) {
        if (noStopRequested) {
          e.printStackTrace();
          stopRequest();
        }
      }

      System.out.println("in CalcWroker - leaving runWork()");
    }
  }

  public void stopRequest() {
    System.out.println("in CalcWorker - entering stopRequest()");
    noStopRequested = false;
    internalThread.interrupt();

    if (sockIn != null) {
      try {
        sockIn.close();
      } catch (IOException e) {

      } finally {
        sockIn = null;
      }
    }

    System.out.println("in CalcWorker - leaving stopRequest()");
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }
}
