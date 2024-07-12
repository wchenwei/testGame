package com.hm.redis;

import lombok.Data;

@Data
public class GuildRedisData {
    public GuildRedisData() {
    }

    private int id;
    private String guildName;    //军团名字
    private int guildFlag;//军团旗帜
    private String flagName;//军团旗帜的字
}
