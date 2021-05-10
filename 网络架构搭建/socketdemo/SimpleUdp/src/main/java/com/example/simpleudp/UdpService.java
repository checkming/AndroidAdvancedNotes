package com.example.simpleudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpService {
     public static void main(String ... args) throws IOException {

          // 创建DatagramSocket 指定一个端口
          DatagramSocket datagramSocket = new DatagramSocket(12307);

          byte[] bytes = new byte[1024];
          //消息数据报
          DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length);

          datagramSocket.receive(datagramPacket);

          System.out.println("接收到的数据： " + new String(datagramPacket.getData()));


     }


}
