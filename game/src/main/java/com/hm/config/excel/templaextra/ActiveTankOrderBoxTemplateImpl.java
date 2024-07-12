package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveTankOrderBoxTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2020/11/17 15:25
 */
@FileConfig("active_tank_order_box")
public class ActiveTankOrderBoxTemplateImpl extends ActiveTankOrderBoxTemplate {
    private List<Items> rewardList = Lists.newArrayList();

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }

    public boolean isFit(int lv){
        return lv>=this.getPlayer_lv_down() &&lv<=this.getPlayer_lv_up();
    }
}
