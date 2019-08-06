package ru.shikhovtsev.javathreadprogramming.chapter05;

public class InterruptReset {
    public static void main(String[] args) {
        System.out.println("Point x: Thread.interrupted = " + Thread.interrupted());

        Thread.currentThread().interrupt();

        System.out.println("Point Y: Thread.interrupted = " + Thread.interrupted());

        System.out.println("Point Z: Thread.interrupted = " + Thread.interrupted());

    }

}
