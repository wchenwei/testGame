package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active816FormulaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_816_formula")
public class Active816FormulaImpl extends Active816FormulaTemplate {
    //月饼奖励
    private List<Items> rewards = Lists.newArrayList();
    //合成月饼消耗
    private List<Items> cost = Lists.newArrayList();

    public void init() {
        this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getProduct());
        this.cost = ItemUtils.str2DefaultItemImmutableList(this.getFormula());
    }

    public List<Items> getRewards() {
        return rewards.stream().map(Items::clone).collect(Collectors.toList());
    }

    public List<Items> getCost() {
        return cost.stream().map(Items::clone).collect(Collectors.toList());
    }

    public boolean checkLv(int lv) {
        return lv >= this.getServer_level_low() && lv <= this.getServer_level_high();
    }
}



