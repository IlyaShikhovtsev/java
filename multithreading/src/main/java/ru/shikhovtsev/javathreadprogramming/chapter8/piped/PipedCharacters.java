package ru.shikhovtsev.javathreadprogramming.chapter8.piped;

import java.io.*;

public class PipedCharacters {
  public static void writeStuff(Writer rawOut) {
    try {
      BufferedWriter out = new BufferedWriter(rawOut);

      String[][] line = {
          {"Java", "has", "nice", "features."},
          {"Pipes", "are", "interesting."},
          {"Threads", "are", "fun", "in", "Java."},
          {"Don’t", "you", "think", "so?"}
      };

      for (int i = 0; i < line.length; i++) {
        String[] word = line[i];

        for (int j = 0; j < word.length; j++) {
          if (j > 0) {
            out.write(" ");
          }

          out.write(word[j]);
        }

        out.newLine();
      }

      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void readStuff(Reader rawIn) {
    try {
      var in = new BufferedReader(rawIn);

      String line;
      while ((line = in.readLine()) != null) {
        System.out.println("read line: " + line);
      }

      System.out.println("read all data from the pipe");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    try {
      final PipedWriter out = new PipedWriter();

      final PipedReader in = new PipedReader(out);

      Runnable runA = () -> writeStuff(out);
      Thread threadA = new Thread(runA, "threadA");
      threadA.start();

      Runnable runB = () -> readStuff(in);
      Thread threadB = new Thread(runB, "threadB");
      threadB.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
