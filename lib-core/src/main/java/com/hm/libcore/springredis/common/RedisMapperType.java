package com.hm.libcore.springredis.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-09-14 17:51
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface RedisMapperType {

    //存储映射类型
    MapperType type();//没有默认字段，必须填写否者就报错

    //存储redis db 默认 0
    int db() default 0;

    String collName() default "";//如果为空,此值为class.getSimpleName();

    //是否压缩
    boolean isZip() default false;

}
