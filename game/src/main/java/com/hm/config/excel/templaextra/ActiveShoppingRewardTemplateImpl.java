package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveShoppingRewardTemplate;

/**
 * @author wyp
 * @description
 * @date 2020/10/15 14:30
 */
@FileConfig("active_shopping_reward")
public class ActiveShoppingRewardTemplateImpl extends ActiveShoppingRewardTemplate {

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
