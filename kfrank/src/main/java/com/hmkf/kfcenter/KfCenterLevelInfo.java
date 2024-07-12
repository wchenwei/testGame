package com.hmkf.kfcenter;

import com.hmkf.gametype.SnumTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KfCenterLevelInfo {
    private int snum;//期数
    private int skilTypeId;//坦克类型技能id
    private long startTime;//开始时间
    private long endTime;//结束时间


    public boolean isOpen() {
        return isOpen(System.currentTimeMillis());
    }

    public boolean isOpen(long now) {
        return now >= this.startTime && now <= this.endTime;
    }

//	public void loadTime() {
//		this.startTime = DateUtil.beginOfWeek(new Date()).getTime();
//		this.endTime = this.startTime + KFLevelConstants.SWeek*(7*GameConstants.DAY);
//	}

    public void loadTime(SnumTime snumTime) {
        this.snum = snumTime.getId();
        this.startTime = snumTime.getStartTime();
        this.endTime = snumTime.getEndTime();
    }
}
