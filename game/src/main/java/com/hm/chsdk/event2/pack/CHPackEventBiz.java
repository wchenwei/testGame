package com.hm.chsdk.event2.pack;

import com.hm.actor.ActorDispatcherType;
import com.hm.chsdk.ICHEvent;
import com.hm.chsdk.event2.CHObserverBiz2;

/**
 * 打包发送
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/24 18:18
 */
public class CHPackEventBiz {
    public static CHEventPack nowPack = new CHEventPack();

    public static void addEvent(ICHEvent event) {
        if (!CHObserverBiz2.isOpen) {
            return;
        }
        nowPack.addEvent(event);
        if (nowPack.isFull()) {
            sendCHSdk();
        }
    }

    public static void sendCHSdk() {
        ActorDispatcherType.CHEventSend.putTask(nowPack);
        nowPack = new CHEventPack();
    }

    public static void stopServer() {
        try {
            sendCHSdk();
        } catch (Exception e) {
        }
    }

}
