package ru.shikhovtsev.javathreadprogramming.chapter18;

import java.beans.IntrospectionException;

public class ObjectFIFO {
  private Object[] queue;
  private int capacity;
  private int size;
  private int head;
  private int tail;

  public ObjectFIFO(int cap) {
    capacity = (cap > 0) ? cap : 1;
    queue = new Object[capacity];
    head = 0;
    tail = 0;
    size = 0;
  }

  public int getCapacity() {
    return capacity;
  }

  public synchronized  int getSize() {
    return size;
  }

  public synchronized boolean isEmpty() {
    return (size == 0);
  }

  public synchronized boolean isFull() {
    return (size == capacity);
  }

  public synchronized void add(Object obj) throws InterruptedException {

    waitWhileFull();

    queue[head] = obj;
    head = (head + 1) % capacity;
    size++;

    notifyAll();
  }

  public synchronized void addEach(Object[] list) throws InterruptedException {
    for (int i = 0; i < list.length; i++) {
      add(list[i]);
    }
  }

  public synchronized Object remove() throws InterruptedException {
    waitWhileEmpty();

    Object obj = queue[tail];
  }
}
