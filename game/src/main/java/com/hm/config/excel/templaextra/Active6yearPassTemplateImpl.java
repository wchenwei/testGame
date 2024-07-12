package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active6yearPassTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_6year_pass")
public class Active6yearPassTemplateImpl extends Active6yearPassTemplate {
    List<Items> free = Lists.newArrayList();
    List<Items> trump = Lists.newArrayList();


    public void init(){
        free = ItemUtils.str2ItemList(this.getReward_free(), ",", ":");
        trump = ItemUtils.str2ItemList(this.getReward_trump(), ",", ":");
    }

    public List<Items> getFreeReward() {
        return free;
    }
    public List<Items> getTrumpReward() {
        return trump;
    }

    public boolean isFit(int stage) {
        return stage == this.getStage();
    }
}
