package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveArmyRaceRechargeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 军备竞赛 充值返利
 * @author: chenwei
 * @create: 2020-03-03 11:14
 **/
@FileConfig("active_army_race_recharge")
public class ActiveArmyRaceRechargeTemplateImpl extends ActiveArmyRaceRechargeTemplate {
    private List<Items> rewards = Lists.newArrayList();

    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",",":");
    }

    public List<Items> getRewards() {
        return rewards;
    }

    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
