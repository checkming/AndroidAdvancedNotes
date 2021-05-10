package com.xiangxue.ch02.memleak;

/**
 * @author 【享学课堂】 King老师
 * 内存泄漏
 */
public class UseStack {

    public static void main(String[] args) {
        Stack stack = new Stack();  //new一个栈
        Object o = new Object(); //new一个对象
        System.out.println("o="+o);
        stack.push(o); //入栈
        Object o1 =  stack.pop(); //出栈
        System.out.println("o1="+o1);
        
        System.out.println(stack.elements[0]); //打印栈中的数据

    }


}
