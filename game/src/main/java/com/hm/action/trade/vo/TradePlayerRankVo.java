package com.hm.action.trade.vo;

import com.hm.redis.PlayerRedisData;
import lombok.Data;

@Data
public class TradePlayerRankVo implements Comparable<TradePlayerRankVo>{
    public long playerId;
    public String name;
    public String icon;
    public int frameIcon;
    public long combat;
//    private String serverName;
    private long st;

    public TradePlayerRankVo(long playerId) {
        this.playerId = playerId;
//        this.serverName = ServerNameCache.getServerName(ServerUtils.getCreateServerId(playerId));
    }

    public void load(PlayerRedisData playerInfo) {
        this.name = playerInfo.getName();
        this.icon = playerInfo.getIcon();
        this.frameIcon = playerInfo.getFrameIcon();
        this.combat = playerInfo.getCombat();
    }


    public void setCombat(long combat) {
        if(combat > 0) {
            this.combat = combat;
        }
    }

    @Override
    public int compareTo(TradePlayerRankVo o) {
        return this.combat - o.getCombat() > 0?1:-1;
    }
}
