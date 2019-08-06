package ru.shikhovtsev.javathreadprogramming.chapter03;

public class TwoThread extends Thread {

    private Thread creatorThread;

    public TwoThread() {
        creatorThread = Thread.currentThread();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            printMsg();
        }
    }

    public void printMsg() {
        Thread t = Thread.currentThread();
        if(t == creatorThread) {
            System.out.println("Creator Thread");
        } else if (t == this) {
            System.out.println("New Thread");
        } else {
            System.out.println("Mystery thread - unexpected");
        }
    }

    public static void main(String[] args) {
        TwoThread tt = new TwoThread();
        tt.start();

        for (int i = 0; i < 10; i++) {
            tt.printMsg();
        }
    }

}
