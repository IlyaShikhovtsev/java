package ru.shikhovtsev.javathreadprogramming.chapter4.secondCounterRunnable;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JComponent;

public class SecondCounterRunnable extends JComponent implements Runnable {

    private volatile boolean keepRunning;
    private Font paintFont;
    private volatile String timeMsg;
    private volatile int arcLen;

    public SecondCounterRunnable() {
        paintFont = new Font("SansSefir", Font.BOLD, 14);
        timeMsg = "never started";
        arcLen = 0;
    }

    public void run() {
        runClock();
    }

    public void runClock() {
        System.out.println("thread running runClock() is " + Thread.currentThread().getName());

        DecimalFormat fmt = new DecimalFormat("0.000");
        long normalSleepTime = 100;
        int counter = 0;
        keepRunning = true;

        while (keepRunning) {
            try {
                Thread.sleep(normalSleepTime);
            } catch (InterruptedException e) {
            }

            counter++;
            double counterSecs = counter / 10.0;

            timeMsg = fmt.format(counterSecs);

            arcLen = (((int) counterSecs) % 60) * 360;
            repaint();
        }
    }

    public void stopClock() {
        keepRunning = false;
    }

    public void paint(Graphics g) {
        System.out.println("thread that invoked paint() is " + Thread.currentThread().getName());

        g.setColor(Color.BLACK);
        g.setFont(paintFont);
        g.drawString(timeMsg, 0, 15);

        g.fillOval(0, 20, 100, 100);

        g.setColor(Color.white);
        g.fillOval(3, 23, 94, 94);

        g.setColor(Color.BLUE);
        g.fillArc(2, 22, 96, 96, 90, -arcLen);
    }
}
