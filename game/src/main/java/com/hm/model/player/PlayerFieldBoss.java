package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.libcore.msg.JsonMsg;

import java.util.Date;

public class PlayerFieldBoss extends PlayerDataContext{
    private transient String dayMark;
    private transient long maxHurt;// 最大伤害值
    private int fightCount;// 挑战次数
    private int freeCount;// 免费次数 看广告


    public boolean checkAndFight(int maxCount){
        if (freeCount > 0){
            this.freeCount --;
            SetChanged();
            return true;
        }
        if(fightCount < maxCount){
            this.fightCount ++;
            SetChanged();
            return true;
        }
        return false;
    }

    public void addFreeCount(){
        this.freeCount ++;
        SetChanged();
    }

    public void resetDay(){
        this.fightCount = 0;
        this.freeCount = 0;
        this.maxHurt = 0;
        updateTodayMark();
    }

    private void updateTodayMark() {
        this.dayMark = RankRedisUtils.createDateMark(new Date());
        SetChanged();
    }

    public boolean updateMaxHurt(long hurt, String dayMark){
        if (StrUtil.isEmpty(this.dayMark)){
            updateTodayMark();
        }
        if (dayMark.equals(this.dayMark) && hurt > maxHurt){
            this.maxHurt = hurt;
            SetChanged();
            return true;
        }
        return false;
    }


    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerFieldBoss", this);
    }
}
