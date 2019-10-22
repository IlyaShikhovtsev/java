package ru.shikhovtsev;

import java.io.*;

public class AAndBProblem {
  public static void main(String[] args) throws IOException {
    new AAndBProblem().run();
  }

  private StreamTokenizer in;

  private int nextInt() throws IOException
  {
    in.nextToken();
    return (int)in.nval;
  }

  private void run() throws IOException
  {
    in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    System.out.print(nextInt() + nextInt());
  }
}
