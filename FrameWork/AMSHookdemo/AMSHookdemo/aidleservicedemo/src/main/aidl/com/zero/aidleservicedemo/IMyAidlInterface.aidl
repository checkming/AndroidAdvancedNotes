// IMyAidlInterface.aidl
package com.zero.aidleservicedemo;

// Declare any non-default types here with import statements

interface IMyAidlInterface {

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    int sendData(String data);
    String getData();
}
