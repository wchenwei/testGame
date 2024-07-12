package com.hm.mq.msg;

import com.hm.libcore.util.gson.GSONUtils;
import com.hg.mq.IMqProxy;
import com.hg.mq.MqType;
import com.hg.mq.enums.MqGroupIdEnum;
import com.hg.mq.enums.MqTagEnum;
import com.hg.mq.rocket.consumer.ConsumerMsgMode;
import com.hm.mq.game.GameMqConfig;
import com.hm.mq.redis.RedisRankLog;
import lombok.Getter;


/**
 * mq的管理类
 *
 * @author cliff
 * @date 2021/4/28
 */
public class MqManager {
    private static final MqManager instance = new MqManager();
    @Getter
    private IMqProxy mqProxy;

    public static MqManager getInstance() {
        return instance;
    }

    private MqManager() {
    }

    public IMqProxy initMqProxy(MqGroupIdEnum mqGroupIdEnum) {
        GameMqConfig.initMqConfig();
        this.mqProxy = MqType.createDefaultMq();
        this.mqProxy.startProduct();
        return this.mqProxy;
    }


    /**
     * 发mq信息
     *
     * @param msg     要发送的消息
     * @param tagEnum 消息发送的目标tag
     */
    public void sendMessageToBizServer(String topicName, InnerMsg msg) {
        try {
            String data = GSONUtils.ToJSONString(msg);
            RedisRankLog.showLog(topicName + "->" + data);
            this.mqProxy.sendMsg(data, ConsumerMsgMode.CLUSTERING, topicName, MqTagEnum.BIZ.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void shutdown() {
        this.mqProxy.shutdown();
    }
}
