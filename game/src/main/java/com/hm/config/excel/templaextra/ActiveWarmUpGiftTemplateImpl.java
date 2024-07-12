package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveWarmUpGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2020/9/14 9:21
 */
@FileConfig("active_warm_up_gift")
public class ActiveWarmUpGiftTemplateImpl extends ActiveWarmUpGiftTemplate {
    List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward_free(), ",", ":");
    }
    public List<Items> getRewards() {
        return rewards;
    }
    public boolean isFit(int lv) {
        return lv>=this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
    }
}
