package com.hmkf.level.rank;

import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hmkf.redis.KFRankRedisUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class RankGroup {
    private String rankId;
    private int id;
    private int levelType;//段位
    private long time = System.currentTimeMillis();//创建事件

    private long playerCount;//玩家人数

    public RankGroup(String rankId) {
        this.rankId = rankId;
        this.levelType = Integer.parseInt(rankId.split("_")[1]);
        this.id = Integer.parseInt(rankId.split("_")[2]);
    }

    public void checkPlayerCount() {
        this.playerCount = KFRankRedisUtils.getRankCount(this.rankId);
    }


    public void addPlayerCount() {
        this.playerCount ++;
    }

    public void buildRankId(int groupId) {
        String week = TimeUtils.formatSimpeTime2(DateUtil.beginOfWeek(new Date()));
        this.rankId = "kfrank"+groupId+"_"+levelType+"_"+id+"_"+week;
    }

    public long getCreateDay() {
        return DateUtil.betweenDay(new Date(time),new Date(),true)+1;
    }

}
