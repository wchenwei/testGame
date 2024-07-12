package com.hm.model;

import com.hm.libcore.util.date.DateUtil;
import com.hm.enums.ChatRoomType;
import com.hm.redis.GuildRedisData;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.RedisUtil;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Data
public class Player extends PlayerSession {
    private String name;//昵称
    private String icon;
    private int frameIcon;//当前头像框
    private int lv;
    private int titleId;//
    private int guildId;//所属部落id
    private int gflag;//部落旗帜

    private transient int vipLv;
    private transient int camp;//阵营
    @Transient
    private transient int areaId;
    @Transient
    private transient String roomId;
    @Transient
    private transient PlayerRedisData playerRedisData;
    @Transient
    private transient long lastChatTime;
    @Transient
    private transient long lastBlackTime; //上次进小黑屋时间
    @Transient
    private transient int todayBlackCount; //今日进小黑屋次数

    public Player() {

    }

    public Player(long playerId) {
        setId(playerId);
    }

    public void setPlayerInfo(PlayerRedisData playerRedisData) {
        this.playerRedisData = playerRedisData;
        this.setServerId(playerRedisData.getServerid());
        this.icon = playerRedisData.getIcon();
        this.frameIcon = playerRedisData.getFrameIcon();
        this.lv = playerRedisData.getLv();
        this.name = playerRedisData.getName();
        this.vipLv = playerRedisData.getVipLv();
        this.titleId = playerRedisData.getTitleId();
        this.camp = playerRedisData.getCamp();
        this.areaId = playerRedisData.getAreaId();
        this.guildId = playerRedisData.getGuildId();
    }

    public void initPlayerData() {
        PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(getId());
        if (playerRedisData != null) {
            setPlayerInfo(playerRedisData);
        }
        if (this.guildId > 0) {
            GuildRedisData guildRedisData = RedisUtil.getGuildRedisData(this.guildId);
            if (guildRedisData != null) {
                setGuildInfo(guildRedisData);
            }
        }
    }

    public void setGuildInfo(GuildRedisData guildRedisData) {
        this.gflag = guildRedisData.getGuildFlag();
    }

    public String getGuildRoomId() {
        return ChatRoomType.getGuildRoomId(getServerId(), this.guildId);
    }

    public String getCampRoomId() {
        return ChatRoomType.getCampRoomId(getServerId(), this.camp);
    }

    public String getAreaRoomId() {
        return ChatRoomType.getAreaRoomId(getServerId(), camp, areaId);
    }

    public boolean isCanChat() {
        return true;
    }

    public void addChatTimeRecord(long time) {
        this.lastChatTime = time;
//		chatTimeRecords.add(time);
//		if (chatTimeRecords.size() > GameConstants.CHAT_TIME_RECORD_MAX) {
//		    chatTimeRecords.removeFirst();
//		}
    }

    public int getTodayBlackCount() {
        if (lastBlackTime <= 0) {
            return 0;
        }
        if (!DateUtil.isSameDay(new Date(lastBlackTime), new Date())) {
            todayBlackCount = 0;
        }
        return todayBlackCount;
    }

    public void addBlackRecord() {
        todayBlackCount = getTodayBlackCount() + 1;
        lastBlackTime = System.currentTimeMillis();
    }
}
