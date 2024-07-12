package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRechargeTurningendTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@FileConfig("active_recharge_turningend")
public class ActiveRechargeTurningendTemplateImpl extends ActiveRechargeTurningendTemplate {
    @Getter
    private List<Items> rewards = Lists.newArrayList();
    public void init() {
        this.rewards = ItemUtils.str2DefaultItemImmutableList(getReward());
    }

    public boolean isFitLv(int lv) {
        return lv >= getLv_down() && lv <= getLv_up();
    }
}
