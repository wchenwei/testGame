package com.hm.model.player;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * pve难度降低buff
 */
@Setter
@Getter
@NoArgsConstructor
public class PveLoseBuff {
    private int type;
    public double rate;//降低百分比
    public long loseTime;//上次时间事件

    public PveLoseBuff(int type) {
        this.type = type;
    }

    public boolean doCheckStart() {
        if(loseTime <= 0) {
            return false;
        }
        if(System.currentTimeMillis() - loseTime < GameConstants.MainBuffLoseTime* GameConstants.MINUTE) {
            return false;
        }
        double[] vals = SpringUtil.getBean(CommValueConfig.class).getCommonValueByDoubles(CommonValueType.MainMissionBuff);
        this.loseTime = System.currentTimeMillis();
        this.rate += vals[0];
        this.rate = Math.min(this.rate,vals[1]);
        return true;
    }

    public void doWin() {
        if(canDel()) {
            return;
        }
        double[] vals = SpringUtil.getBean(CommValueConfig.class).getCommonValueByDoubles(CommonValueType.MainMissionBuff);
        this.loseTime = 0L;
        if(this.rate > vals[2]) {
            this.rate -= vals[2];
        }
        this.rate = Math.max(0,this.rate);
    }

    public boolean canDel() {
        return this.loseTime <= 0 && this.rate <= 0;
    }
}
