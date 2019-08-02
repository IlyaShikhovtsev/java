package ru.shikhovtsev.javathreadprogramming.chapter4.secondCounterInaccurate;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JComponent;

public class SecondCounterInaccurate extends JComponent implements Runnable {

    private volatile boolean keepRunning;
    private Font paintFont;
    private volatile String timeMsg;
    private volatile int arcLen;

    public SecondCounterInaccurate() {
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
        long startTime = System.currentTimeMillis();
        keepRunning = true;

        while (keepRunning) {
            try {
                Thread.sleep(normalSleepTime);
            } catch (InterruptedException e) {
            }

            counter++;
            double counterSecs = counter / 10.0;
            double elapsedSecs = (System.currentTimeMillis() - startTime)/1000.0;

            double diffSecs = counterSecs - elapsedSecs;
            timeMsg = fmt.format(counterSecs) + " - " + fmt.format(elapsedSecs) + " = " + fmt.format(diffSecs);

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
