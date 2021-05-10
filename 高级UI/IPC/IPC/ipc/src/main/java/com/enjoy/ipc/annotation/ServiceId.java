package com.enjoy.ipc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lance
 * @date 2019/1/8
 */

//元注解： 注解上的注解


//保留
@Retention(RetentionPolicy.RUNTIME) //给反射用
//目标
@Target(ElementType.TYPE)
public @interface ServiceId {
    String value();
}
