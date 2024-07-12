package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRechargeCircleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 充值转盘
 * @author: chenwei
 * @create: 2019-12-23 18:56
 **/
@FileConfig("active_recharge_circle")
public class ActiveRechargeCircleTemplateImpl extends ActiveRechargeCircleTemplate {

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
