package com.xiangxue.ch02;

/**
 * @author 【享学课堂】 King老师
 * 堆中内存分配和回收-大对象直接进入老年代
 * -Xms20m -Xmx20m  -Xmn10m -XX:+PrintGCDetails
 */
public class BigAllocation {
    private static final int _1MB =1024*1024; //1M的大小
    // * 大对象直接进入老年代
    public static void main(String[] args) {
        byte[] allocation1,allocation2,allocation3;
        allocation1 = new byte[2*_1MB]; //根据信息可以知道 2M的数组大约占据 3M的空间
        allocation2 = new byte[3*_1MB];//大对象直接进入老年代（3M的数组大约占据 3M的空间）
    }

}
