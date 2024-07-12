package com.hm.libcore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wyp
 * @description
 * @date 2021/11/29 15:37
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PeriodsMark {
    // 一共有多少期（处理多少个）
    // 例如:@FileConfig("search_map_{5}")  处理后缀为 1~5 的JSON数据
    int value() default 1;
}
