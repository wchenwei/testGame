package com.hmkf.redis;

import cn.hutool.core.convert.Convert;
import com.hmkf.config.KFLevelConstants;
import lombok.Data;
import redis.clients.jedis.Tuple;

@Data
public class RankItem {
    private String playerId;
    private double score;
    private int rank;//排名

    public RankItem(Tuple tuple) {
        this.playerId = tuple.getElement();
        this.score = tuple.getScore();
    }

    public boolean isNpc() {
        return KFLevelConstants.isNpc(this.playerId);
    }

    public int getIntId() {
        return Convert.toInt(this.playerId,0);
    }
}
