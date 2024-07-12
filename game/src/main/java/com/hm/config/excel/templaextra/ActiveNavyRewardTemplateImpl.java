package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveNavyRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 海军节
 * @author: chenwei
 * @create: 2020-03-31 10:33
 **/
@FileConfig("active_navy_reward")
public class ActiveNavyRewardTemplateImpl extends ActiveNavyRewardTemplate {

    private List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }
    public List<Items> getRewards() {
        return rewards;
    }

    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
