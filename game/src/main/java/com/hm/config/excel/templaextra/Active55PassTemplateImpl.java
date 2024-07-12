package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active55PassTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @ClassName Active55PassTemplateImpl
 * @Deacription TODO
 * @Author zxj
 * @Date 2022/5/20 11:45
 * @Version 1.0
 **/
@FileConfig("active_55_pass")
public class Active55PassTemplateImpl extends Active55PassTemplate {
    List<Items> free = Lists.newArrayList();
    List<Items> trump = Lists.newArrayList();
    List<Items> legend = Lists.newArrayList();


    public void init(){
        free = ItemUtils.str2ItemList(this.getReward_free(), ",", ":");
        trump = ItemUtils.str2ItemList(this.getReward_trump(), ",", ":");
        legend = ItemUtils.str2ItemList(this.getReward_legend(), ",", ":");
    }

    public List<Items> getFreeReward() {
        return free;
    }
    public List<Items> getTrumpReward() {
        return trump;
    }
    public List<Items> getLegendReward() {
        return legend;
    }

    public boolean isFit(int stage) {
        return stage == this.getStage();
    }

    public boolean isFit(int version, int playerLv) {
        return version == this.getStage() && playerLv >= this.getPlayer_lv_down() && playerLv <= this.getPlayer_lv_up();
    }
}
