package com.hm.model.guild.bean;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuildPlayerSimpleVo implements Comparable<GuildPlayerSimpleVo>{
    private long playerId;
    private int guildJob;
    private long combat;
    private long lastLoginTime;

    public GuildPlayerSimpleVo(GuildPlayer guildP) {
        this.playerId = guildP.getPlayerId();
        this.guildJob = guildP.getGuildJob();
        PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(guildP.getPlayerId());
        this.lastLoginTime = playerRedisData.getLastLoginDate();
        this.combat = playerRedisData.getCombat();
    }

    @Override
    public int compareTo(GuildPlayerSimpleVo o) {
        return Long.compare(o.getCombat(), combat);
    }
}
