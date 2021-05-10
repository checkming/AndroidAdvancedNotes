package com.arch.demo.core.model;

import java.io.Serializable;

public class BaseCachedData<T> implements Serializable {
    public long updateTimeInMills;
    public T data;
}
