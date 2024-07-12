package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveArmyRaceProgressTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 军力奖励
 * @author: chenwei
 * @create: 2020-03-05 09:48
 **/
@FileConfig("active_army_race_progress")
public class ActiveArmyRaceProgressTemplateImpl extends ActiveArmyRaceProgressTemplate {
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
