package com.hm.model.guild.bean;

import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GuildReqPlayer extends GuildPlayer{
    private long endTime;// 申请过期时间

    public GuildReqPlayer(Player player, long endTime) {
        super(player);
        this.endTime = endTime;
    }

    public boolean isEnd(){
        return this.endTime < System.currentTimeMillis();
    }
}
