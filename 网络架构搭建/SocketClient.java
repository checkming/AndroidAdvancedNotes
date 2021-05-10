package com.example.simplesocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {


     public static void main(String ... args) throws IOException {
          // 1 创建一个客户端Socket 指定服务器的ip地址和端口
          Socket socket = new Socket("127.0.0.1",12346);
          // 2 获取输出流，向服务器发送信息
          OutputStream os = socket.getOutputStream();//向服务器写信息
          PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
          pw.write("Hello 服务器");
          pw.flush();
          socket.shutdownOutput();//关闭输出流
          socket.close();
     }
}
