package com.enjoy.memory;

import android.content.Context;

public class Manager {
    //GC ROOT
    private static final Manager ourInstance = new Manager();

    static String i = "aa";


    private Context mContext;

    public static Manager getInstance() {
        return ourInstance;
    }

    private Manager() {
    }

    public void init(Context context){
       mContext = context;
    }
}
