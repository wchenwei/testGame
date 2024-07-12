package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSscarRewardTemplate;

/**
 * Description:
 * User: yang xb
 * Date: 2019-02-13
 */
@FileConfig("active_SScar_reward")
public class ActiveSscarRewardTemplateImpl extends ActiveSscarRewardTemplate {
    /**
     * 获取获得展示坦克
     * @return
     */
    public int getActivityTankId(int choiceType) {
    	String mainReward = getMain_reward();
    	if(StrUtil.isNotEmpty(mainReward)) {
    		int type = Integer.parseInt(mainReward.split(":")[0]);
    		if(type == choiceType) {
    			return Integer.parseInt(mainReward.split(":")[1]);
    		}
    	}
    	return 0;
    }
}
