package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRichmanProgressTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_richman_progress")
public class ActiveRichmanProgressTemplateImpl extends ActiveRichmanProgressTemplate {
    private List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }

    public List<Items> getRewards() {
        return rewards;
    }

    public boolean inLv(int serverLv) {
        return getServer_lv_down() <= serverLv && serverLv <= getServer_lv_up();
    }
}
