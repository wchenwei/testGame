package com.hm.model.player;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.MissionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.enums.CommonValueType;
import lombok.Data;

/**
 * 主线关卡失败buff
 */
@Data
public class MissionLoseBuff {
    public double rate;//降低百分比
    public long loseTime;//上次时间事件

    public boolean doCheckStart(int missionId) {
        if(loseTime <= 0) {
            return false;
        }
        //判断是否时主线关卡
        MissionConfig missionConfig = SpringUtil.getBean(MissionConfig.class);
        MissionExtraTemplate template = missionConfig.getMission(missionId);
        if(template == null || !template.isMainMission()) {
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

    public void doWin(int missionId) {
        //判断是否时主线关卡
//        MissionConfig missionConfig = SpringUtil.getBean(MissionConfig.class);
//        MissionExtraTemplate template = missionConfig.getMission(missionId);
//        if(template == null || !template.isMainMission()) {
//            return;
//        }
        if(this.loseTime <= 0 && this.rate <= 0) {
            return;
        }
        double[] vals = SpringUtil.getBean(CommValueConfig.class).getCommonValueByDoubles(CommonValueType.MainMissionBuff);
        this.loseTime = 0L;
        if(this.rate > vals[2]) {
            this.rate -= vals[2];
        }
    }
}
