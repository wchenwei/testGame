package com.hmkf.model;

import com.hm.rpc.KFPlayerSession;

import com.hm.libcore.msg.JsonMsg;
import com.hmkf.db.KfDBUtils;
import com.hmkf.db.KfPlayerUtils;
import com.hm.message.KFRankMessageComm;
import com.hmkf.redis.KFRankRedisUtils;

/**
 * 玩家跨服数据
 *
 * @author xiaoaogame
 */
public abstract class KFBasePlayer extends KFPlayerSession implements IPKPlayer{
    private int gameTypeId;

    private LevelPlayer levelPlayer = new LevelPlayer();//段位信息
    private LevelPlayerInfo levelPlayerInfo = new LevelPlayerInfo();//段位信息


    public LevelPlayerInfo getLevelPlayerInfo() {
        return levelPlayerInfo;
    }

    public LevelPlayer getLevelPlayer() {
        this.levelPlayer.LateInit(this);
        return levelPlayer;
    }


    public int getLevelType() {
        return this.levelPlayer.getLevelType();
    }

    public void fillMsg(JsonMsg msg, boolean ignoreChange) {
        msg.addProperty("id", getId());
        msg.addProperty("levelPlayerInfo", levelPlayerInfo);
        msg.addProperty("levelPlayerGroup", levelPlayer);
    }


    public void sendPlayerUpdate() {
        sendPlayerUpdate(true);
    }

    public void sendPlayerUpdate(boolean isSave) {
        JsonMsg serverMsg = JsonMsg.create(KFRankMessageComm.S2C_KfPlayer);
        fillMsg(serverMsg, true);
        sendMsg(serverMsg);

        if (isSave) save();
    }

    public String getRankId() {
        return levelPlayer.getRankId();
    }

    public void save() {
        KfDBUtils.saveOrUpdate(this);
    }

    public int getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(int gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public int getGuildId() {
        return KfPlayerUtils.getGuildId(getId());
    }

    @Override
    public void addScore(long add) {
        this.levelPlayerInfo.addScore(add,0);
    }

    @Override
    public long getScore() {
        return this.levelPlayerInfo.getScore();
    }

    @Override
    public String getPlayerId() {
        return getId()+"";
    }
}
