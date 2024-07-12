package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveGiftDayTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_gift_day")
public class ActiveGiftDayTemplateImpl extends ActiveGiftDayTemplate {
    private List<Items> itemList = Lists.newArrayList();

    @ConfigInit
    public void init(){
        this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getItemList() {
        return itemList;
    }

    public boolean isFit(int lv) {
        return lv >= getPlayer_lv_down() && lv <= getPlayer_lv_up();
    }

    public boolean isFree(){
        return this.getType() == 1;
    }
}
