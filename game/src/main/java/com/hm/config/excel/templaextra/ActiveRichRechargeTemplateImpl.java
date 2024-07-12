package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRichRechargeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_rich_recharge")
public class ActiveRichRechargeTemplateImpl extends ActiveRichRechargeTemplate {
    private List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }

    public List<Items> getRewards(){
        return this.rewards.stream().map(t ->t.clone()).collect(Collectors.toList());
    }
}
