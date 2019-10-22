package ru.shikhovtsev.T1001;

import java.io.*;

public class T1001 {

  /*public static void main(String[] args) throws IOException {
    byte[] str = new byte[System.in.available()];
    System.in.read(str);

    String[] numbers = new String(str).split("\\D");
    for (int i = numbers.length - 1; i >= 0; --i) {
      if (!numbers[i].equals("")) {
        System.out.write(String.format("%1$.4f\n", Math.sqrt(Double.parseDouble(numbers[i]))).getBytes());
      }
    }
  }*/

  //fixme Не понимаю почему не срабатывает, разобраться!
  /*public static void main(String[] args) throws IOException {
    StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//    StreamTokenizer in = new StreamTokenizer(new BufferedReader(new FileReader("/Users/shikhovtsev/IdeaProjects/java/timus/src/main/java/ru/shikhovtsev/T1001/input.txt")));

    double[] array = new double[32];

    int lastIndex = -1;

    while (in.nextToken() != StreamTokenizer.TT_EOF) {
      lastIndex++;
      array[lastIndex] = in.nval;
    }
    for (; lastIndex >= 0; lastIndex--) {
      System.out.print(String.format("%.4f\n", Math.sqrt(array[lastIndex])));
    }
  }*/
}
