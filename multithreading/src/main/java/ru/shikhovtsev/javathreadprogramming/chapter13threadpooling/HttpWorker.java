package ru.shikhovtsev.javathreadprogramming.chapter13threadpooling;

import ru.shikhovtsev.javathreadprogramming.chapter18.ObjectFIFO;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class HttpWorker {
  private static int nextWorkerID = 0;

  private File docRoot;
  private ObjectFIFO<HttpWorker> idleWorkers;
  private int workerId;
  private ObjectFIFO<Socket> handoffBox;

  private Thread internalThread;
  private volatile boolean noStopRequested;

  public HttpWorker(File docRoot, int workerPriority, ObjectFIFO<HttpWorker> idleWorkers) {
    this.docRoot = docRoot;
    this.idleWorkers = idleWorkers;

    workerId = getNextWorkerID();
    handoffBox = new ObjectFIFO<>(1);

    noStopRequested = true;

    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    internalThread = new Thread(r);
    internalThread.setPriority(workerPriority);
    internalThread.start();
  }

  public static synchronized int getNextWorkerID() {
    return nextWorkerID++;
  }

  public void processRequest(Socket s) throws InterruptedException {
    handoffBox.add(s);
  }

  private void runWork() {
    Socket s = null;
    InputStream in = null;
    OutputStream out = null;

    while (noStopRequested) {
      try {
        idleWorkers.add(this);

        s = handoffBox.remove();

        in = s.getInputStream();
        out = s.getOutputStream();
        generateResponse(in, out);
        out.flush();
      } catch (IOException iox) {
        System.err.println("I/O error while processing request, ignoring and adding back to idle queue - workerId = " + workerId);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {

          } finally {
            in = null;
          }
        }

        if (out != null) {
          try {
            out.close();
          } catch (IOException e) {

          } finally {
            out = null;
          }
        }

        if (s != null) {
          try {
            s.close();
          } catch (IOException e) {

          } finally {
            s = null;
          }
        }
      }
    }
  }

  private void generateResponse(InputStream in, OutputStream out) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    String requestLine = reader.readLine();
    if ((requestLine == null) || (requestLine.length() < 1)) {
      throw new IOException("could not read reaquest");
    }

    System.out.println("workerId=" + workerId + "requestLine=" + requestLine);

    StringTokenizer st = new StringTokenizer(requestLine);
    String filename;

    try {
      st.nextToken();
      filename = st.nextToken();
    } catch (NumberFormatException e) {
      throw new IOException("could not parse request line");
    }

    File requestedFile = generateFile(filename);

    BufferedOutputStream outBuff = new BufferedOutputStream(out);

    if (requestedFile.exists()) {
      System.out.println("workerId = " + workerId + ", 200 OK: " + filename);

      int fileLen = (int) requestedFile.length();

      BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(requestedFile));

      String contentType = URLConnection.guessContentTypeFromStream(fileIn);

      byte[] headerBytes = createHeaderBytes("HTTP/1.0 200 OK", fileLen, contentType);

      outBuff.write(headerBytes);

      byte[] buf = new byte[2048];
      int blockLen = 0;

      while ((blockLen = fileIn.read(buf)) != -1) {
        outBuff.write(buf, 0, blockLen);
      }

      fileIn.close();
    } else {
      System.out.println("workerId = " + workerId + ", 404 Not Found: " + filename);

      byte[] headerBytes = createHeaderBytes("HTTP/1.0 404 Not Found", -1, null);

      outBuff.write(headerBytes);
    }

    outBuff.flush();
  }

  private File generateFile(String filename) {
    File requestedFile = docRoot;

    StringTokenizer st = new StringTokenizer(filename, "/");

    while (st.hasMoreTokens()) {
      String tok = st.nextToken();

      if (tok.equalsIgnoreCase("..")) {
        continue;
      }

      requestedFile = new File(requestedFile, tok);
    }

    if (requestedFile.exists() && requestedFile.isDirectory()) {
      requestedFile = new File(requestedFile, "index.html");
    }

    return requestedFile;
  }

  private byte[] createHeaderBytes(String resp, int contentLen, String contentType) throws IOException {
    var baos = new ByteArrayOutputStream();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));

    writer.write(resp + "\r\n");

    if (contentLen != -1) {
      writer.write("Content-Length: " + contentLen + "\r\n");
    }

    if (contentType != null) {
      writer.write("Content-Type: " + contentType + "\r\n");
    }

    writer.write("\r\n");
    writer.flush();

    byte[] data = baos.toByteArray();
    writer.close();
    return data;
  }

  public void stopRequest() {
    noStopRequested = false;
    internalThread.interrupt();
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }
}
