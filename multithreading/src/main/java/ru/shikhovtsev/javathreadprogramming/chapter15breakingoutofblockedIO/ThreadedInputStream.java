package ru.shikhovtsev.javathreadprogramming.chapter15breakingoutofblockedIO;

import ru.shikhovtsev.javathreadprogramming.chapter16surestop.SureStop;
import ru.shikhovtsev.javathreadprogramming.chapter18.ByteFIFO;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

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

  private void signalEOF() {
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
    System.arraycopy(workBuf, 0, addBuf, 0, addBuf.length);

    buffer.add(addBuf);
  }

  private void stopRequest() {
    if (noStopRequested) {
      noStopRequested = false;
      internalThread.interrupt();
    }
  }

  @Override
  public void close () throws IOException {
    if (closeRequested) {
      return;
    }

    signalClose();

    SureStop.ensureStop(internalThread, 10000);
    stopRequest();

    final InputStream localIn = in;
    Runnable r = () -> {
      try {
        localIn.close();
      } catch (IOException e) {

      }
    };

    Thread t = new Thread(r, "in-close");
    t.setDaemon(true);
    t.start();
  }

  private void throwExceptionIfClosed() throws IOException {
    if(closeRequested) {
      throw new IOException("stream is closed");
    }
  }

  @Override
  public int read() throws InterruptedIOException, IOException {
    byte[] data = new byte[1];
    int ret = read(data, 0, 1);

    if (ret != 1) {
      return -1;
    }

    return data[0] & 0x00000FF;
  }

  @Override
  public int read(byte[] dest) throws InterruptedIOException, IOException {
    return read(dest, 0, dest.length);
  }

  @Override
  public int read(byte[] dest, int offset, int length) throws InterruptedIOException, IOException {
    throwExceptionIfClosed();

    if (length < 1) {
      return 0;
    }

    if ((offset < 0) || ((offset + length) > dest.length)) {
      throw new IllegalArgumentException("offset must be at least 0, and ");
    }

    byte[] data = removeUpTo(length);

    if (data.length > 0) {
      System.arraycopy(data, 0, dest, offset, data.length);
      return data.length;
    }

    if (eofDetected) {
      return -1;
    }

    stopRequest();

    if (ioxMessage == null) {
      ioxMessage = "stream cannot be read";
    }

    throw new IOException(ioxMessage);
  }

  private byte[] removeUpTo(int maxRead) throws IOException {
    try {
      synchronized (buffer) {
        while (buffer.isEmpty() && !eofDetected && !ioxDetected && !closeRequested) {
          buffer.wait();
        }
      }

      throwExceptionIfClosed();

      byte[] data = buffer.removeAll();

      if (data.length > maxRead) {
        byte[] putBackData = new byte[data.length - maxRead];
        System.arraycopy(data, maxRead, putBackData, 0, putBackData.length);
        buffer.add(putBackData);

        byte[] keepData = new byte[maxRead];
        System.arraycopy(data, 0, keepData, 0, keepData.length);
        data = keepData;
      }

      return data;
    } catch (InterruptedException e) {
      throw new InterruptedIOException("interrupted reading");
    }
  }

  public long skip(long n) throws IOException {
    throwExceptionIfClosed();

    if (n <= 0) {
      return 0;
    }

    int skipLen = (int) Math.min(n, Integer.MAX_VALUE);
    int readCount = read(new byte[skipLen]);

    return readCount;
  }

  public int available() throws IOException {
    throwExceptionIfClosed();
    return buffer.getSize();
  }

  public boolean markSupported() {
    return false;
  }

  public synchronized void mark(int readLimit) {

  }

  public synchronized void reset() throws IOException {
    throw new IOException("mark-reset not supported on this stream");
  }
}
