package com.xiangxue.ch02;

import java.util.LinkedList;
import java.util.List;


 /**
 * @author 【享学课堂】 King老师
 * VM Args：-Xms30m -Xmx30m  -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 * 堆内存溢出
 */
public class Oom {

    public static void main(String[] args) {
        List<Object> list = new LinkedList<>(); //在方法执行的过程中，它是GCRoots
        int i =0;
        while(true){
            i++;
            if(i%10000==0) System.out.println("i="+i);
            list.add(new Object());
        }

        //String[] strings = new String[100000000];

    }

}
