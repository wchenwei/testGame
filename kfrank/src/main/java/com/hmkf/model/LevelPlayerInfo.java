package com.hmkf.model;

import com.hm.config.GameConstants;
import com.hm.libcore.util.GameIdUtils;
import com.hmkf.action.level.vo.KfPlayerDetailVo;
import com.hmkf.config.KFLevelConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

@Setter
@Getter
public class LevelPlayerInfo extends KFPlayerDataContext {
    private transient KfPlayerDetailVo[] matchInfos;
    private transient long matchTime;

    private int win;
    private int lose;
    private int continueWin;//连胜
    private int maxContinueWin;//最高连胜
    private long score;
    private int fightNum;
    private int freeNum = 1;//免费次数

    private int dayBuyNum;//今日购买次数
    private String recordMark;

    private transient String dayMark;

    public void addKfFightNum(boolean isWin) {
        if (isWin) {
            this.win++;
            this.continueWin++;
            this.maxContinueWin = Math.max(this.maxContinueWin, this.continueWin);
        } else {
            this.lose++;
            this.continueWin = 0;
        }
        reduceFightNum();
        SetChanged();
    }

    public void addScore(long add, long MIN_SCORE) {
        this.score += add;
        this.score = Math.max(this.score, MIN_SCORE);
        if(add < 0) {
            this.recordMark = GameIdUtils.nextStrId();
        }
        SetChanged();
    }

    public void setScore(long score) {
        this.score = score;
        SetChanged();
    }


    public long getScore() {
        return score;
    }

    public int getFightNum() {
        return fightNum;
    }

    public void dayReset() {
        this.freeNum = KFLevelConstants.DayCount;
        this.dayBuyNum = 0;
        SetChanged();
    }

    public void weekReset() {
        this.win = 0;
        this.lose = 0;
        this.continueWin = 0;
        this.maxContinueWin = 0;
        this.matchInfos = null;
        this.recordMark = null;
        SetChanged();
    }

    public String getDayMark() {
        return dayMark;
    }

    public void buyCount(int add) {
        this.dayBuyNum += add;
        this.fightNum += add;
        SetChanged();
    }

    public void setDayMark(String dayMark) {
        this.dayMark = dayMark;
        SetChanged();
    }

    public KfPlayerDetailVo[] getMatchInfos() {
        if(this.matchInfos != null) {
            for (int i = 0; i < this.matchInfos.length; i++) {
                if(this.matchInfos[i] == null) {
                    this.matchInfos[i] = Arrays.stream(this.matchInfos).filter(Objects::nonNull).findFirst().orElse(null);
                }
            }   
        }
        return matchInfos;
    }

    public void setMatchInfos(KfPlayerDetailVo[] matchInfos) {
        this.matchInfos = matchInfos;
        if(this.matchInfos == null) {
            this.matchTime = 0;
        }else{
            this.matchTime = System.currentTimeMillis()+ GameConstants.MINUTE*5;
        }
        SetChanged();
    }

    public boolean isCanRefreshRank() {
        return System.currentTimeMillis() > this.matchTime;
    }


    public boolean haveFightNum() {
        return freeNum > 0 || fightNum > 0;
    }
    public void reduceFightNum() {
        SetChanged();
        if(freeNum > 0) {
            freeNum -- ;
            return;
        }
        if(fightNum > 0) {
            fightNum -- ;
            return;
        }
    }
}
