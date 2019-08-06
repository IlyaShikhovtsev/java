package ru.shikhovtsev.javathreadprogramming.chapter05;

public class PiInterrupt implements Runnable {
    private double latestPiEstimate;

    @Override
    public void run() {
        try {
            System.out.println("for comparision, Math.Pi= " + Math.PI);
            calcPi(0.0000001);
        System.out.println("within accuracy, latest pi = " + latestPiEstimate);
        } catch (InterruptedException e) {
            System.out.println("INTERRUPTED!! latest pi = "+ latestPiEstimate);
        }
    }

    private void calcPi(double accuracy) throws InterruptedException {
        latestPiEstimate = 0.0;
        long iteration = 0;
        int sign = -1;

        while (Math.abs(latestPiEstimate - Math.PI) > accuracy) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            iteration++;
            sign = -sign;
            latestPiEstimate += sign * 4.0 / ((2 * iteration) - 1);
        }
    }

    public static void main(String[] args) {
        PiInterrupt pi = new PiInterrupt();
        Thread t = new Thread(pi);
        t.start();

        try {
            Thread.sleep(10000);
            t.interrupt();
        } catch (InterruptedException e) {
        }
    }
}
