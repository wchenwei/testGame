package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveShoppingGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 *          限时秒杀
 * @date 2020/10/14 19:46
 */
@FileConfig("active_shopping_gift")
public class ActiveShoppingGiftTemplateImpl extends ActiveShoppingGiftTemplate {
    private List<Items> rewards = Lists.newArrayList();
    // 消耗金砖数
    private List<Items> usedPrice = Lists.newArrayList();

    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
        this.usedPrice = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
    }

    public List<Items> getRewards() {
        return rewards;
    }

    public List<Items> getUsedPrice() {
        return usedPrice;
    }

    public boolean isFit(int days, int serverLv,int type) {
        return this.getDays()==days && serverLv >=this.getServer_lv_down() && serverLv <= this.getServer_lv_up() && this.getType() == type;
    }

    public boolean isFitDays(int days,int type) {
        return this.getDays()==days && this.getType() == type;
    }

}
