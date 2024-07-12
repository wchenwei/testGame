package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveCvoutTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/2/5 12:31
 */
@FileConfig("active_cvout")
public class ActiveCvoutTemplateImpl extends ActiveCvoutTemplate {

    private List<Items> itemCost = Lists.newArrayList();

    private List<Items> itemGoldExtra = Lists.newArrayList();

    public void init() {
        itemCost = ItemUtils.str2ItemList(getCost(), ",", ":");
        itemGoldExtra = ItemUtils.str2ItemList(getCost_gold_extra(), ",", ":");
    }

    public List<Items> getItemCost() {
        return itemCost;
    }

    public void setItemCost(List<Items> itemCost) {
        this.itemCost = itemCost;
    }

    public boolean isStage(int stage) {
        return getStage() == stage;
    }

    public List<Items> getItemGoldExtra() {
        return itemGoldExtra;
    }
}
