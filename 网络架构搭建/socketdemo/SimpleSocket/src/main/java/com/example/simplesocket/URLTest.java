package com.example.simplesocket;

import java.net.MalformedURLException;
import java.net.URL;

import javax.print.DocFlavor;

public class URLTest {

     public static void main(String ... args) throws MalformedURLException {

         //创建一个URL实例
         URL baidu = new URL("https://www.baidu.com");

         //?后面表示参数，#表示锚点
         URL url = new URL(baidu,"/s?wd=jdk文档");

         System.out.println("协议: " + url.getProtocol());
         System.out.println("主机: " + url.getHost());
         //如果未指定端口号，则使用默认的端口号，此时getPort返回-1
         System.out.println("端口: " + url.getPort());
         System.out.println("文件路径:" + url.getPath());
         System.out.println("相对路径: " + url.getRef());
         System.out.println("查询字符串: " + url.getQuery());



     }
}
