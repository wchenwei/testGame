package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GachaConfigTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-18
 */
@FileConfig("gacha_config")
public class GachaConfigTemplateImpl extends GachaConfigTemplate {
    private Items cost;
    private Items giftReward;

    public void init() {
        cost = ItemUtils.str2Item(getCost_item(), ":");
        giftReward = ItemUtils.str2Item(getGift_reward(), ":");
    }
    public Items getCost() {
        return cost;
    }

    public Items getGiftReward() {
        return giftReward;
    }
}
