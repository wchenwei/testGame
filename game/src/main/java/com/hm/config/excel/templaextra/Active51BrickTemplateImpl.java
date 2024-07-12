package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.temlate.Active51BrickTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

/**
 * @description:地块元素
 * @author: chenwei
 * @create: 2020-04-02 15:53
 **/
@FileConfig("active_51_brick")
public class Active51BrickTemplateImpl extends Active51BrickTemplate {
    private Items rewards;

    public void init(){
        this.rewards = ItemUtils.str2Item(getReward(),":");
    }

    public Items getRewards() {
        return rewards;
    }

    public boolean isCanHurt(){
        return getCanHurt() == 1;
    }

    public boolean isTreasure() {
        return getType() == GameConstants.Act51_Brick_Treasure;
    }
}
