package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveWeekShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_week_shop")
public class ActiveWeekShopTemplateImpl extends ActiveWeekShopTemplate {
    List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getGoods());
    }
    public List<Items> getRewards() {
        return rewards;
    }
}
