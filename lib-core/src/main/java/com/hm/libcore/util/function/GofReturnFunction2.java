package com.hm.libcore.util.function;


@FunctionalInterface
public interface GofReturnFunction2<R, T1, T2> {

    R apply(T1 t1, T2 t2);

}
