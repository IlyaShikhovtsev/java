package ru.shikhovtsev.javathreadprogramming.chapter11.squish;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Squish extends JComponent {
  private Image[] frameList;
  private long msPerFrame;
  private volatile int currFrame;

  private Thread internalThread;
  private volatile boolean noStopRequested;

  public Squish(int width, int height, long msPerCycle, int framesPerSec, Color fgColor) {
    setPreferredSize(new Dimension(width, height));

    int framesPerCycle = (int) ((framesPerSec * msPerCycle) / 1000);

    msPerFrame = 1000L / framesPerSec;

    frameList = buildImages(width, height, fgColor, framesPerCycle);
    currFrame = 0;

    noStopRequested = true;
    Runnable r = () -> {
      try {
        runWork();
      } catch (Exception x) {
        x.printStackTrace();
      }
    };

    internalThread = new Thread(r);
    internalThread.start();
  }

  private Image[] buildImages(int width, int height, Color color, int count) {
    BufferedImage[] im = new BufferedImage[count];

    for (int i = 0; i < count; i++) {
      im[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      double xShape = 0.0;
      double yShape = ((double) (i * height)) / (double) count;

      double wShape = width;
      double hShape = 2.0 * (height - yShape);
      Ellipse2D shape = new Ellipse2D.Double(xShape, yShape, wShape, hShape);

      Graphics2D g2 = im[i].createGraphics();
      g2.setColor(color);
      g2.fill(shape);
      g2.dispose();
    }

    return im;
  }

  private void runWork() {
    while (noStopRequested) {
      currFrame = (currFrame + 1) % frameList.length;
      repaint();

      try {
        Thread.sleep(msPerFrame);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void stopRequest() {
    noStopRequested = false;
    internalThread.interrupt();
  }

  public boolean isAlive() {
    return internalThread.isAlive();
  }

  public void paint(Graphics g) {
    g.drawImage(frameList[currFrame], 0, 0, this);
  }
}
