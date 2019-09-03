package ru.shikhovtsev.javathreadprogramming.chapter12;

public class ExceptionCallbackMain implements ExceptionListener {

  private int exceptionCount;

  public ExceptionCallbackMain() {
    exceptionCount = 0;
  }

  public void exceptionOccurred(Exception x, Object source) {
    exceptionCount++;
    System.err.println("Exception #" + exceptionCount + ", source = " + source);
    x.printStackTrace();
  }

  public static void main(String[] args) {
    ExceptionListener l = new ExceptionCallbackMain();
    ExceptionCallback ec = new ExceptionCallback(l);
  }
}
