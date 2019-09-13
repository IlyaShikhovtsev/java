package ru.shikhovtsev.javathreadprogramming.chapter13threadpooling;

import ru.shikhovtsev.javathreadprogramming.chapter18.ObjectFIFO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

  private ObjectFIFO<HttpWorker> idleWorkers;

  private HttpWorker[] workerList;
  private ServerSocket ss;

  private Thread internalThread;
  private volatile boolean noStopRequested;

  public HttpServer(File docRoot,
                    int port,
                    int numberOfWorkers,
                    int maxPriority) throws IOException {

    ss = new ServerSocket(port, 10);

    if ((docRoot == null) || !docRoot.exists() || !docRoot.isDirectory()) {
      throw new IOException("Specified docRoot is null or does not exist or is not a directory");
    }

    numberOfWorkers = Math.max(1, numberOfWorkers);

    int serverPriority = Math.max(Thread.MIN_PRIORITY + 2, Math.min(maxPriority, Thread.MAX_PRIORITY - 1));

    int workerPriority = serverPriority - 1;

    idleWorkers = new ObjectFIFO(numberOfWorkers);
    workerList = new HttpWorker[numberOfWorkers];

    for (int i = 0; i < numberOfWorkers; i++) {
      workerList[i] = new HttpWorker(docRoot, workerPriority, idleWorkers);
    }

    noStopRequested = true;

    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception x) {
        x.printStackTrace();
      }
    };

    internalThread = new Thread(r);
    internalThread.setPriority(serverPriority);
    internalThread.start();
  }

  private void runWork() {
    System.out.println("HttpServer ready to receive requests");

    while (noStopRequested) {
      try {
        Socket s = ss.accept();

        if (idleWorkers.isEmpty()) {
          System.out.println("HttpServer too busy, denying request");

          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

          writer.write("HTTP/1.0 503 Service Unavailable\r\n\r\n");

          writer.flush();
          writer.close();
          writer = null;
        } else {
          HttpWorker worker = idleWorkers.remove();
          worker.processRequest(s);
        }
      } catch (IOException e) {
        if (noStopRequested) {
          e.printStackTrace();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void stopRequest() {
    noStopRequested = false;
    internalThread.interrupt();

    for (int i = 0; i < workerList.length; i++) {
      workerList[i].stopRequest();
    }

    if (ss != null) {
      try {
        ss.close();
      } catch (IOException iox) {
        ss = null;
      }
    }
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }

  private static void usageAndExit(String msg, int exitCode) {
    System.err.println(msg);
    System.err.println("Usage: java HttpServer <port> <numWorkers> <documentRoot>");
    System.err.println("    <port> - port to listen on for HTTP requests");
    System.err.println("    <numWorkers> - number of worker threads to create");
    System.err.println("    <documentRoot> - base directory for HTML files");
    System.exit(exitCode);
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      usageAndExit("wrong number of arguments", 1);
    }

    String portStr = args[0];
    String numWorkersStr = args[1];
    String docRootStr = args[2];

    int port = 0;

    try {
      port = Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
      usageAndExit("could not parse port number from '" + portStr + "'", 2);
    }

    if (port < 1) {
      usageAndExit("invalid port number specified: " + port, 3);
    }

    int numWorkers = 0;
    try {
      numWorkers = Integer.parseInt(numWorkersStr);
    } catch (NumberFormatException e) {
      usageAndExit("could not parse number of workers from '" + numWorkersStr + "'", 4);
    }

    File docRoot = new File(docRootStr);

    try {
      new HttpServer(docRoot, port, numWorkers, 6);
    } catch (IOException e) {
      e.printStackTrace();
      usageAndExit("could not construct HttpServer", 5);
    }
  }
}
