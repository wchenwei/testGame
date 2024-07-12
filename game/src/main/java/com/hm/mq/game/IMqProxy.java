package com.hm.mq.game;

import com.hm.action.cmq.CmqMsg;

import java.util.List;

/**
 * 发送代理
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/8/2 8:59
 */
public interface IMqProxy {
    void sendMessage(String queueName, CmqMsg msg);

    void sendMessage(String queueName, List<CmqMsg> msgs);

    void sendDefaultMessage(CmqMsg msg);


    public static IMqProxy createMqProxy() {
        return new SelfMqProxy();
//        return new CmqProxy();
    }
}
