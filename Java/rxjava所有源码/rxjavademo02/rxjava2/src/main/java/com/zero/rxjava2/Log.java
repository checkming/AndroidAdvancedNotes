package com.zero.rxjava2;

public class Log {

    public static void sleep(long i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void v(String tag,String str){
        System.out.println(tag + ": " + str);
    }

    public static void i(String tag,String str){
        System.out.println(tag + ": " + str);
    }

    public static void d(String tag,String str){
        System.out.println(tag + ": " + str);
    }

    public static void w(String tag,String str){
        System.out.println(tag + ": " + str);
    }

    public static void e(String tag,String str){
        System.out.println(tag + ": " + str);
    }
}
