package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveShoppingLibraryTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

/**
 * @author wyp
 * @description
 *          狂欢盛典商品库
 * @date 2020/10/15 11:33
 */
@FileConfig("active_shopping_library")
public class ActiveShoppingLibraryTemplateImpl extends ActiveShoppingLibraryTemplate {
    private Items rewardItems;

    public void init() {
        rewardItems = ItemUtils.str2Item(getReward(), ":");
    }

    public Items getRewardItems() {
        return rewardItems;
    }

    public boolean isFit(int lv){
        return lv>= this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
    }
}
