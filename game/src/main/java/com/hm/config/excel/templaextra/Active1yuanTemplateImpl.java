package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active1yuanTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_1yuan")
public class Active1yuanTemplateImpl extends Active1yuanTemplate {
    private List<Items> itemList = Lists.newArrayList();

    public void init() {
        this.itemList = ItemUtils.str2ItemList(getRecharge(), ",", ":");
    }

    public List<Items> getItemList() {
        return itemList;
    }
}
