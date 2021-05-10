package com.xiangxue.network.rxbus;

import java.lang.reflect.Method;

import io.reactivex.Scheduler;


public class SubscriberMethod {
    public Scheduler getThreadMode() {
        return threadMode;
    }

    final Scheduler threadMode;
    final Method method;
    final Class<?> eventType;

    public SubscriberMethod(Method method, Class<?> eventType, Scheduler threadMode) {
        this.method = method;
        this.eventType = eventType;
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
