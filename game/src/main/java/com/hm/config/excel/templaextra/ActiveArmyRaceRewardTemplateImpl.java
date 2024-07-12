package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveArmyRaceRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 军备竞赛 订单奖励
 * @author: chenwei
 * @create: 2020-03-03 11:18
 **/
@FileConfig("active_army_race_reward")
public class ActiveArmyRaceRewardTemplateImpl extends ActiveArmyRaceRewardTemplate {
    private List<Items> rewards = Lists.newArrayList();

    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",",":");
    }

    public List<Items> getRewards() {
        return rewards;
    }

    public boolean isFit(int lv, int stage) {
        return lv>=getLv_down()&&lv<=getLv_up() && this.getStage() == stage;
    }
}
