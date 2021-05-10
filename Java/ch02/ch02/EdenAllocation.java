package com.xiangxue.ch02;

/**
 * @author 【享学课堂】 King老师
 * 堆中内存分配和回收
 * -Xms20m -Xmx20m  -Xmn10m -XX:+PrintGCDetails
 */
public class EdenAllocation {
    private static final int _1MB =1024*1024; //1M的大小
    // * 对象优先在Eden分配
    public static void main(String[] args) {
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[1*_1MB]; //根据信息可以知道 1M的数组大约占据 1.5M的空间(对象头，对象数据、填充)
        allocation2 = new byte[1*_1MB];
        allocation3 = new byte[1*_1MB];
        allocation4 = new byte[1*_1MB];
    }

}
