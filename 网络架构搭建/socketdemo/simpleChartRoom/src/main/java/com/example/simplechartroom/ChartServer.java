package com.example.simplechartroom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChartServer {

    private ServerSocket server = null; //服务器的ServerSocket
    private static final int PORT = 10065;
    private List<Socket> mClients = new ArrayList<>();
    private ExecutorService mExec = null;

     public static void main(String ... args){
            new ChartServer();
     }

    public ChartServer(){
        //开启服务
        System.out.println("服务器运行中。。。");
        try {
            server = new ServerSocket(PORT);
            //创建一个线程池
            mExec = Executors.newCachedThreadPool();
            Socket client = null;
            while (true){
                System.out.println("等待客户上门。。。");
                client = server.accept();
                System.out.println("有客户来了。。。 ,客户是： " + client.getInetAddress());
                mClients.add(client);
                mExec.execute(new Service(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable{
         private Socket socket;

         private BufferedReader br = null;
         private String msg = "";

         public Service(Socket socket){
             this.socket = socket;
             try {
                 br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 this.sendMsg();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

         public void sendMsg(){//会给客户端的消息
             int num = mClients.size();
             OutputStream os = null;//向服务器写信息
             try {
                 os = socket.getOutputStream();
                 PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
                 pw.write("你好 你是第"+ num + "个客户");
                 pw.flush();
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }

        @Override
        public void run() {
            while(true){
                try {
                    if(( msg = br.readLine())!=null){
                        System.out.println("客户端说：" + msg);
                        if("bye".equals(msg)){//应用自己定义的协议
                            socket.close();
                            break;
                        }else{
                            sendMsg();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}