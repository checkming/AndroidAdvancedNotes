package com.xiangxue.ch02;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class StopWorld {

    /*不停往list中填充数据  业务线程*/
    public static class FillListThread extends Thread{
        List<byte[]> list = new LinkedList<>();
        //模拟业务不断填充数据局
        @Override
        public void run() {
            try {
                while(true){
                    if(list.size()*512/1024/1024>=990){ //数据达到一定，进行清理
                        list.clear();
                        System.out.println("list is clear");
                    }
                    byte[] bl;
                    for(int i=0;i<100;i++){
                        bl = new byte[512]; //填充数据
                        list.add(bl);  //填充数据
                    }
                    Thread.sleep(1);
                }

            } catch (Exception e) {
            }
        }
    }

    /*每100ms定时打印*/
    public static class TimerThread extends Thread{
        public final static long startTime = System.currentTimeMillis();
        @Override
        public void run() {
            try {
                while(true){
                    long t =  System.currentTimeMillis()-startTime;
                    System.out.println(t/1000+"."+t%1000);  //每个一段时间打印一下业务执行时间
                    Thread.sleep(100); //0.1秒 0.1 -0.2 -0.3
                }
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        FillListThread myThread = new FillListThread(); //业务线程
        TimerThread timerThread = new TimerThread();  //打印时间线程(0.1秒打印一次)
        myThread.start();
        timerThread.start();
    }

}
