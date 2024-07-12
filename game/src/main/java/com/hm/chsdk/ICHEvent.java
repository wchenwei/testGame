package com.hm.chsdk;

/**
 * 草花事件
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/2/18 10:30
 */
public interface ICHEvent {
    String buildUrl();

    Class buildClass();

    default boolean isUploadEvent2() {
        return false;
    }
}
