package ru.shikhovtsev.javathreadprogramming.chapter15breakingoutofblockedIO;

import ru.shikhovtsev.javathreadprogramming.chapter18.ByteFIFO;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThreadedInputStream extends FilterInputStream {
  private ByteFIFO buffer;

  private volatile boolean closeRequested;
  private volatile boolean eofDetected;
  private volatile boolean ioxDetected;
  private volatile String ioxMessage;

  private Thread internalThread;
  private volatile boolean noStopRequested;

  protected ThreadedInputStream(InputStream in, int bufferSize) {
    super(in);

    buffer = new ByteFIFO(bufferSize);
    closeRequested = false;
    eofDetected = false;
    ioxDetected = false;
    ioxMessage = null;

    noStopRequested = true;
    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    internalThread = new Thread(r);
    internalThread.setDaemon(true);
  }

  public ThreadedInputStream(InputStream in) {
    this(in, 2048);
  }

  private void runWork() {
    byte[] workBuf = new byte[buffer.getCapacity()];

    try {
      while (noStopRequested) {

        int readCount = in.read(workBuf);

        if (readCount == -1) {
          signalEOF();
          stopRequest();
        } else if (readCount > 0) {
          addToBuffer(workBuf, readCount);
        }
      }
    } catch (IOException e) {
      if (!closeRequested) {
        ioxMessage = e.getMessage();
        signalIOX();
      }
    } catch (InterruptedException e) {

    } finally {
      signalEOF();
    }
  }

  private void signalEof() {
    synchronized (buffer) {
      eofDetected = true;
      buffer.notifyAll();
    }
  }

  private void signalIOX() {
    synchronized (buffer) {
      ioxDetected = true;
      buffer.notifyAll();
    }
  }

  private void signalClose() {
    synchronized (buffer) {
      closeRequested = true;
      buffer.notifyAll();
    }
  }

  private void addToBuffer (byte[] workBuf, int readCount) throws InterruptedException {
    byte[] addBuf = new byte[readCount];
  }
}
