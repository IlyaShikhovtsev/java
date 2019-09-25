package ru.shikhovtsev.concurrentptogramminginjava.chapter1.particles;

import java.awt.*;
import java.util.Random;

public class Particle {
  private int x;
  private int y;
  private final Random random = new Random();

  public Particle(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public synchronized void move() {
    x += random.nextInt(10) - 5;
    x += random.nextInt(20) - 10;
  }

  public void draw(Graphics g) {
    int lx, ly;
    synchronized (this) {
      lx = x;
      ly = y;
    }
    g.drawRect(lx, ly, 10, 10);
  }
}
