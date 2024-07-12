package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveShoppingRechargeOnceTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2020/10/15 11:38
 */
@FileConfig("active_shopping_recharge_once")
public class ActiveShoppingRechargeOnceTemplateImpl extends ActiveShoppingRechargeOnceTemplate {
    private List<Items> rewards = Lists.newArrayList();

    public List<Items> getRewards() {
        return rewards;
    }

    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }

    public boolean isFit(int lv){
        return lv>= this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
    }
}
