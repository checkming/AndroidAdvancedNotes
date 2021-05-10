package com.example.simpleudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClient {
     public static void main(String ... args) throws IOException {
             String msg = "Hello sevice";
          DatagramSocket datagramSocket = new DatagramSocket();

          //创建datagramPacket发送信息
          DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(),msg.getBytes().length, InetAddress.getLocalHost(),12307);

          datagramSocket.send(datagramPacket);

     }
}
