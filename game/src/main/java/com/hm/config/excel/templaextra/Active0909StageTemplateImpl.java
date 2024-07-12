package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0909StageTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_0909_stage")
public class Active0909StageTemplateImpl extends Active0909StageTemplate {
    private List<Items> costAttackBossItems = Lists.newArrayList();
    private List<Items> rewardCostGoldItems = Lists.newArrayList();

    public void init() {
        this.costAttackBossItems = ItemUtils.str2ItemList(getCost_attack_boss(), ",", ":");
        this.rewardCostGoldItems = ItemUtils.str2ItemList(getReward_cost_gold(), ",", ":");
    }


    public List<Items> getCostAttackBossItems() {
        return costAttackBossItems;
    }

    public List<Items> getRewardCostGoldItems() {
        return rewardCostGoldItems;
    }
}
