package com.hm.mq.redis;

import com.hm.actor.ActorDispatcherType;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.leaderboards.AbstractRankCmq;
import com.hm.libcore.spring.SpringUtil;

import java.util.Map;

/**
 * 阿里云mq排行榜发送器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/16 16:16
 */
public class RedisRankSender extends AbstractRankCmq {
    public static int weight = 100;
    private RankBiz rankBiz;

    public void startProducer() {
        this.rankBiz = SpringUtil.getBean(RankBiz.class);
        this.rankBiz.init();
    }

    @Override
    public void sendRankCmq(Map<String, Object> payload) {
        ActorDispatcherType.RankCmq.putTask(new IRunner() {
            @Override
            public Object runActor() {
                String command = (String) payload.get("name");
                String[] args = (String[]) payload.get("args");
                rankBiz.execScript(command, args);
                return null;
            }
        });
    }
}
