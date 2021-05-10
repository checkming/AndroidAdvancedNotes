package com.example.simplesocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class URLTest01 {

     public static void main(String ... args) throws IOException {

         URL url = new URL("http://www.baidu.com");
         //通过URL的openStream方法获取URL对象所表示的资源的字节输入流
         InputStream is = url.openStream();
         InputStreamReader isr = new InputStreamReader(is,"utf-8");
         //为子符输入流添加缓冲
         BufferedReader br = new BufferedReader(isr);

         String data = br.readLine();//读取数据
         while(data!=null){
             System.out.println(data);
             data= br.readLine();
         }
         br.close();
         isr.close();
         is.close();

     }
}
