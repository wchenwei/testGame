package com.hm.libcore.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 例如：@ServerIntervalJob(200)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServerIntervalJob {
    long value() default 0;
}