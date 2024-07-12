package com.hm.annotation;

import com.hm.observer.ObservableEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Broadcast {
    //支持多个信号
    ObservableEnum[] value();

    //可以不填参数，默认都是10000，如需填写参数需要填写到需要填写的位置，其他位置可以填-1，如有5個参数，只需要第四个位置order为10则oder填写为{-1,-1,-1,-1,10}
    //这样解析出来除了第四个位置order为10，其余四个参数order都为10000
    int[] order() default {10000};
}
