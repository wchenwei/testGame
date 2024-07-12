package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.AdRewardTemplate;
import com.hm.enums.AdsType;
import lombok.Data;

@Data
@FileConfig("ad_reward")
public class AdTemplate extends AdRewardTemplate {
    private AdsType adsType;

    public void init() {
        this.adsType = AdsType.getAdsType(getType());
    }

    //是否看完广告直接领取奖励
    public boolean isNowReward() {
        return true;
    }

}
