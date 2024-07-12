package com.hm.libcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来处理配置文件加载顺序(值越大越先加载)
 *
 * @author xjt
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigLoadIndex {
    int value() default Integer.MAX_VALUE;
}
 