package com.zero.reflectdemo.proxy;

public class Client1 {

    /**
     * 静态代理演示
     *
     * @param args
     */
    public static void main(String ... args){
            //TODO:
        //创建Zero
        IShop zero = new Zero();
        //创建代购者av，并将zero作为构造参数传入
        IShop av = new AV(zero);
        av.buy();
    }
}
