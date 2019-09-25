package ru.shikhovtsev.concurrentptogramminginjava.chapter1.particles;

import java.awt.*;

public class ParticleCanvas extends Canvas {
  private Particle[] particles = new Particle[0];

  ParticleCanvas(int size) {
    setSize(new Dimension(size, size));
  }

  public synchronized void setParticles(Particle[] ps) {
    if (ps == null) {
      throw new IllegalArgumentException("cannot set null");
    }
    particles = ps;
  }

  protected synchronized Particle[] getParticles() {
    return particles;
  }

  public void paint(Graphics g) {
    Particle[] ps = getParticles();

    for (int i = 0; i < ps.length; i++) {
      ps[i].draw(g);
    }
  }
}
