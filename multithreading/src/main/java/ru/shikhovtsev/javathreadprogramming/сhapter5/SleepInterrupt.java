package ru.shikhovtsev.javathreadprogramming.сhapter5;

public class SleepInterrupt implements Runnable {

    public void run() {
        try {

            System.out.println("in run() - about to sleep for 20 sec");

            Thread.sleep(20000);

            System.out.println("in run() - woke up");
        } catch (InterruptedException e) {
            System.out.println("in run() - interrupted while sleeping");
            return;
        }

        System.out.println("in run() - doing stuff after nap");

        System.out.println("in run - leaving normally");
    }

    public static void main(String[] args) {
        var si = new SleepInterrupt();
        var t = new Thread(si);
        t.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }


        System.out.println("in main - interrupting other thread");
        t.interrupt();
        System.out.println("in main - leaving");
    }

}
