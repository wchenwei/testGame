package com.hm.kfchat;

import com.hm.kfchat.cache.KFGuildRedisCache;
import com.hm.kfchat.cache.KFPlayerRedisCache;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.ServerNameCache;
import com.hm.redis.data.GuildRedisData;
import com.hm.util.ServerUtils;
import com.hm.util.bluevip.QQBlueVip;
import lombok.Data;

/**
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/3 11:34
 */
@Data
public class KFPlayerInfo {
    public long playerId;
    public int serverId;
    public String name;
    public int guildId;
    public String flag;
    public String icon;
    public int frameIcon;
    public String guildName;
    public int vipLv;

    public String sName;


    public KFPlayerInfo(long playerId) {
        this.playerId = playerId;

        PlayerRedisData playerRedisData = KFPlayerRedisCache.getRedisPlayer(playerId);
        if (playerRedisData != null) {
            load(playerRedisData);
        }
    }

    public void load(PlayerRedisData redisData) {
        this.playerId = redisData.getId();
        this.name = redisData.getName();
        this.serverId = ServerUtils.getServerId(playerId);
        this.guildId = redisData.getGuildId();
        this.vipLv = redisData.getVipLv();
        this.icon = redisData.getIcon();
        this.frameIcon = redisData.getFrameIcon();

        this.sName = ServerNameCache.getServerName(ServerUtils.getCreateServerId(playerId));

        if (this.guildId > 0) {
            GuildRedisData guildRedisData = KFGuildRedisCache.getInstance().getGuild(this.guildId);
            if (guildRedisData != null) {
                this.flag = guildRedisData.getFlagName();
                this.guildName = guildRedisData.getGuildName();
            }
        }
    }


}
