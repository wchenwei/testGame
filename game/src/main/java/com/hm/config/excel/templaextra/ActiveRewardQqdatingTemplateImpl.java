package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRewardQqdatingTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/10/9 20:16
 */
@FileConfig("active_reward_qqdating")
public class ActiveRewardQqdatingTemplateImpl extends ActiveRewardQqdatingTemplate {

    private List<Items> reward = Lists.newArrayList();

    public void init() {
        this.reward = ItemUtils.str2DefaultItemImmutableList(this.getReward());
    }

    public List<Items> getRewardItems() {
        return reward;
    }

    public boolean checkPlayerLv(int playerLv) {
        return this.getLv_down() <= playerLv && this.getLv_up() >= playerLv;
    }
}

