package com.zero.reflectdemo.proxy;

public class AV implements IShop {

    private IShop mShop;

    public AV(IShop shop){
        this.mShop = shop;
    }

    @Override
    public void buy() {
        System.out.println("AV老师代购");
        mShop.buy();
    }
}
