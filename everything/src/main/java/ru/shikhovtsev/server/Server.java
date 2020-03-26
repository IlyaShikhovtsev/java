package ru.shikhovtsev.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  public static void main(String[] args) throws IOException {
    var ss = new ServerSocket(8080);
    while (true) {
      Socket accepted = ss.accept();


      handler(accepted);
//      accepted.close();
    }
  }

  private static void handler(Socket accepted) throws IOException {
    System.out.println(accepted);
    var writer = new PrintWriter(new OutputStreamWriter(accepted.getOutputStream()));
    writer.write("Hello, man!");
    writer.flush();
  }

}
