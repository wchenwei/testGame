package com.hm.libcore.actor.once;

/**
 * 依次执行队列
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/25 17:46
 */
public interface IOnceTask extends Runnable {
    void doOnceTask();

    Object getId();

    default void run() {
        try {
            doOnceTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
