package com.xiangxue.alvin.firstusestruct;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/5/23.
 */

public class HttpHelper {

    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        if (! (genType instanceof ParameterizedType)) {
            return object.getClass();
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
