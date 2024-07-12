package com.hmkf.level.king;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;

public class KingInfo {
    protected int id;
    protected String name;
    protected int server;
    protected long combat;
    private String icon;
    private int frameIcon;//当前头像框

    public KingInfo(int id) {
        super();
        this.id = id;
        reloadInfo();
    }


    public void reloadInfo() {
        this.server = ServerUtils.getServerId(id);
        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(id);
        if (redisData != null) {
            this.name = redisData.getName();
            this.combat = redisData.getCombat();
            this.icon = redisData.getIcon();
            this.frameIcon = redisData.getFrameIcon();
        }
    }
}
