package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.TankMasterRewardTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@Getter
@FileConfig("tank_master_reward")
public class TankMasterRewardTemplateImpl extends TankMasterRewardTemplate {
    private List<Items> rewards = Lists.newArrayList();

    @ConfigInit
    public void init(){
        this.rewards = ItemUtils.str2DefaultItemImmutableList(getReward());
    }

}
