package com.hm.mq.msg;

import com.hm.libcore.leaderboards.AbstractRankCmq;
import com.hm.libcore.util.gson.GSONUtils;
import com.hg.mq.IMqProxy;
import com.hg.mq.rocket.consumer.ConsumerMsgMode;
import com.hm.mq.game.GameMqConfig;
import com.hm.util.RandomUtils;

import java.util.Map;

/**
 * 阿里云mq排行榜发送器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/16 16:16
 */
public class MqRankSender extends AbstractRankCmq {
    private IMqProxy mqProxy;

    public void startProducer() {
        //active Mq
        this.mqProxy = MqManager.getInstance().getMqProxy();
    }


    @Override
    public void sendRankCmq(Map<String, Object> payload) {
        try {
            String body = GSONUtils.ToJSONString(payload);
            String topic = GameMqConfig.getInstance().getRankTopic();
            String tag = "rank";
            this.mqProxy.sendMsg(body, ConsumerMsgMode.CLUSTERING, topic, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
