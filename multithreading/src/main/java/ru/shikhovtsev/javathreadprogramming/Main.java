package ru.shikhovtsev.javathreadprogramming;

public class Main {

    public static void main(String[] args) {
        int a = 5;
        int b = 6;
        change(a, b);
        System.out.println(a + b);
    }

    public static void change(int a, int b) {
        a++;
        b++;
    }

}
