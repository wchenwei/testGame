package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveFlashSaleTicketTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.springframework.data.util.Pair;

import java.util.List;

@FileConfig("active_flash_sale_ticket")
public class ActiveFlashSaleTicketTemplateImpl extends ActiveFlashSaleTicketTemplate {
    private List<Items> itemList = Lists.newArrayList();

    public void init() {
        this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getItemList() {
        return itemList;
    }

    public Pair<Integer, Integer> genGroupKey() {
        return Pair.of(getSever_lv_down(), getSever_lv_up());
    }
}
