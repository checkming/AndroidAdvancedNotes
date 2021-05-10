package com.enjoy.ipc2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {

    //获得单例对象
    public static final int GET_INSTANCE = 0;
    //执行方法
    public static final int GET_METHOD = 1;

    //todo 请求类型
    private int type;


    //请求哪个服务 (ServiceId)
    private String serviceId;
    //请求的方法名
    private String methodName;
    //执行方法的参数
    private Parameters[] parameters;

    public Request(int type, String serviceId, String methodName, Parameters[] parameters) {
        this.type = type;
        this.serviceId = serviceId;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    protected Request(Parcel in) {
        type = in.readInt();
        serviceId = in.readString();
        methodName = in.readString();
        parameters = in.createTypedArray(Parameters.CREATOR);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Parameters[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameters[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(serviceId);
        dest.writeString(methodName);
        dest.writeTypedArray(parameters, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}
