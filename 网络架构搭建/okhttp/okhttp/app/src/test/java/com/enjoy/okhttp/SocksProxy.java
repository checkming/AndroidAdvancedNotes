package com.enjoy.okhttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocksProxy {
    static int state = 0;

    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(808);
        Socket accept = serverSocket.accept();

        InputStream is = accept.getInputStream();
        OutputStream os = accept.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) != -1) {
            //state 代表第几轮握手
            if (state < 2) {
                state = doHandshake(state, os, b, len);
            } else {
                //不需要握手
                System.out.println("转发：\n" + new String(b, 0, len));

                //假设转发完成=======
                StringBuilder response = new StringBuilder();
                response.append("HTTP/1.1 200 OK\r\n");
                response.append("Connection: close\r\n");
                response.append("Content-Type: application/json;charset=UTF-8\r\n");
                response.append("Content-Length: 445\r\n");
                response.append("\r\n");

                response.append("{\"status\":\"1\",\"count\":\"2\",\"info\":\"OK\"," +
                        "\"infocode\":\"10000\",\"lives\":[{\"province\":\"湖南\",\"city\":\"长沙市\"" +
                        ",\"adcode\":\"430100\",\"weather\":\"多云\",\"temperature\":\"32\"," +
                        "\"winddirection\":\"东\",\"windpower\":\"≤3\",\"humidity\":\"59\"," +
                        "\"reporttime\":\"2019-08-13 14:54:21\"}," +
                        "{\"province\":\"湖南\",\"city\":\"长沙县\",\"adcode\":\"430121\"," +
                        "\"weather\":\"多云\",\"temperature\":\"32\",\"winddirection\":\"东\"," +
                        "\"windpower\":\"≤3\",\"humidity\":\"59\",\"reporttime\":\"2019-08-13 14:54:22\"}]}");
                os.write(response.toString().getBytes());
                os.flush();
                os.close();
                break;
            }
        }

    }



    private static int doHandshake(int state, OutputStream os, byte[] b, int len) throws IOException {
        if (state == 0) {
            //第一轮
            //代表版本 0x05代表：socks5
            byte VER = b[0];
            //代表下一个字段长度，代表几种验证方式  okhttp发来：0x02
            byte NMETHODS = b[1];
            // okhttp发来：0x0 0x2 表示可以不认证，也可以用户名、密码认证
            byte METHODS1 = b[2]; //0x00
            byte METHODS2 = b[3]; //0x02

            //告诉okhttp 版本是5，我选择不加密
            os.write(new byte[]{5, 0});
            os.flush();
            return 1;
        } else if (state == 1) {
            //第二轮
            //版本
            byte VER = b[0];
            //命令 0x01 表示连接
            byte CMD = b[1];
            //保留字段 0x00 不管
            byte RSV = b[2];
            // 0x03 表示域名
            byte ATYPE = b[3];

            //域名长度
            byte ADDLEN = b[4];
            String addr = new String(b, 5, ADDLEN);
            System.out.println("域名为：" + addr);


            //端口 只有两字节，但为了转int 给了4字节
            ByteBuffer PORT = ByteBuffer.allocate(2);
            PORT.put(b, 5 + ADDLEN, 2);
            PORT.flip();
            int port = PORT.getShort();
            System.out.println("端口为：" + port);
            //======================本代理服务器与接口服务器连接======================

            /**
             * 回复
             */
            //05版本 + 00成功 + 00保留字段 + 后面的会被忽略掉，直接给到 01域名 + 4字节ipv4+2字节端口
            os.write(new byte[]{0x05, 0x00, 0x00, 0x01, 0x00,0x00,0x00,0x00,0x00,0x00});
            //域名+端口
            os.flush();
            return 2;
        }

        return 0;
    }


}
