package com.hm.mq.game;

import com.hm.action.cmq.CmqMsg;
import com.hm.mq.msg.InnerMsg;
import com.hm.mq.msg.MqManager;

import java.util.List;

/**
 * cmq代理
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/8/2 9:01
 */
public class SelfMqProxy implements IMqProxy {

    @Override
    public void sendMessage(String queueName, CmqMsg msg) {
        MqManager.getInstance().sendMessageToBizServer(queueName, createInnerMsg(msg));
    }

    @Override
    public void sendDefaultMessage(CmqMsg msg) {
        sendMessage(GameMqConfig.getInstance().getDefaultTopic(), msg);
    }

    @Override
    public void sendMessage(String queueName, List<CmqMsg> msgs) {
        for (CmqMsg msg : msgs) {
            sendMessage(queueName, msg);
        }
    }

    public InnerMsg createInnerMsg(CmqMsg msg) {
        InnerMsg innerMsg = new InnerMsg(msg.getMsgId());
        innerMsg.setParamMap(msg.getParamMap());
        return innerMsg;
    }
}
