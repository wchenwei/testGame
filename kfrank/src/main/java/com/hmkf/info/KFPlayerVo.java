package com.hmkf.info;

import com.hm.action.player.vo.PlayerDetailVo;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;

public class KFPlayerVo extends PlayerDetailVo {

    public KFPlayerVo(Player tarPlayer) {
        super(tarPlayer);
    }

    public void load(BasePlayer player) {
        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(player.getId());
        load(redisData);
        loadGuild(player.getServerId());
    }
}
