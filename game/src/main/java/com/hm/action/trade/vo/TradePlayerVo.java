package com.hm.action.trade.vo;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.ServerNameCache;
import com.hm.redis.trade.PlayerStock;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;
import lombok.Data;

@Data
public class TradePlayerVo {
    public long playerId;
    public String name;
    public String icon;
    public int frameIcon;
    public long combat;
    public long val;//上缴或者增收数量
    public String serverName;

    public TradePlayerVo(long playerId) {
        this.playerId = playerId;
        this.serverName = ServerNameCache.getServerName(ServerUtils.getCreateServerId(playerId));
        loadKingInfo();
    }

    public void loadKingInfo() {
        PlayerRedisData playerInfo = RedisUtil.getPlayerRedisData(this.playerId);
        PlayerStock playerStock = PlayerStock.getPlayerStock(this.playerId);
        if(playerInfo != null) {
            this.name = playerInfo.getName();
            this.icon = playerInfo.getIcon();
            this.frameIcon = playerInfo.getFrameIcon();
            this.combat = playerStock.getCombat();
        }
    }
}
