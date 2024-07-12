package com.hm.redis;

import lombok.Data;

@Data
public class PlayerRedisData {
    private long id;
    private int camp;
    private int vipLv;
    private int lv;
    private String name;
    private long combat;
    private String icon;
    private int frameIcon;//当前头像框
    private int titleId;
    private int areaId;
    private int officialType; // 官职
    private long rechargeGold; // 充值钻石数
    private int guildId;
    private int serverid;

    public PlayerRedisData() {
        super();
    }
}
