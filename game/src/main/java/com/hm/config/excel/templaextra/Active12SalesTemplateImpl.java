package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active12SalesTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-11-21
 */
@FileConfig("active_12_sales")
public class Active12SalesTemplateImpl extends Active12SalesTemplate {
    private List<Items> itemList = Lists.newArrayList();

    public void init() {
        this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getItemList() {
        return itemList;
    }
}
