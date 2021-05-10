package com.xiangxue.ch02.memleak;

/**
 * @author 【享学课堂】 King老师
 * 手写一个栈
 */
public class Stack {

    public Object[] elements;
    private int size =0;
    private static final int Cap = 16;

    public Stack() {
        elements = new Object[Cap];
    }

    public void push(Object e){ //入栈
        elements[size] = e;
        size++;
    }

    public Object pop(){  //出栈
    	size = size -1;
        Object o = elements[size];
        elements[size] = null;  //让GC 回收掉
        return o;
    }
}
