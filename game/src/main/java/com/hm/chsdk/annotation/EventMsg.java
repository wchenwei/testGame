package com.hm.chsdk.annotation;

import com.hm.observer.ObservableEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMsg {
    ObservableEnum obserEnum() default ObservableEnum.LOGIN;
}
