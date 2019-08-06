package ru.shikhovtsev.javathreadprogramming.chapter03;

public class TwoThreadAlive extends Thread {

    public void run() {
        for (int i = 0; i < 10; i++) {
            printMsg();
        }
    }

    public void printMsg() {
        Thread t = Thread.currentThread();
        String name = t.getName();
        System.out.println("name=" + name);
    }

    public static void main(String[] args) throws InterruptedException {
        TwoThreadAlive tt = new TwoThreadAlive();
        tt.setName("my worker");

        System.out.println("before start(), tt.isAlive=" + tt.isAlive());
        tt.start();
        System.out.println("just after start(), tt.isAlive=" + tt.isAlive());

        Thread.sleep(100);
        for (int i = 0; i < 10; i++) {
            tt.printMsg();
        }

        System.out.println("at the end of main, tt.isAlive=" + tt.isAlive());
    }

}
