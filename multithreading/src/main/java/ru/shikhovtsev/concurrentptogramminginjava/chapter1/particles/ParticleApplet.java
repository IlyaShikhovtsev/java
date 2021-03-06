package ru.shikhovtsev.concurrentptogramminginjava.chapter1.particles;

import java.applet.Applet;

public class ParticleApplet extends Applet {
  protected Thread[] threads = null;

  private final ParticleCanvas canvas = new ParticleCanvas(100);

  public void init() {
    add(canvas);
  }

  private Thread makeThread(final Particle p) {
    Runnable r = () -> {
      try {
        while (true) {
          p.move();
          canvas.repaint();
          Thread.sleep(100);
        }
      } catch (InterruptedException e) {
        return;
      }
    };

    return new Thread(r);
  }

  public synchronized void start() {
    int n = 10;

    if (threads == null) {
      Particle[] particles = new Particle[n];
      for (int i = 0; i < n; i++) {
        particles[i] = new Particle(50, 50);
      }
      canvas.setParticles(particles);

      threads = new Thread[n];
      for (int i = 0; i < n; i++) {
        threads[i] = makeThread(particles[i]);
        threads[i].start();
      }
    }
  }

  public synchronized void stop() {
    if (threads != null) {
      for (int i = 0; i < threads.length; i++) {
        threads[i].interrupt();
      }
      threads = null;
    }
  }
}
