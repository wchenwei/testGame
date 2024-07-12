package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-04-20
 */
@FileConfig("tec")
public class TecTemplateImpl extends TecTemplate {
    private List<Items> costItems = Lists.newArrayList();

    public void init() {
        if (!getCost().isEmpty()) {
            costItems = ItemUtils.str2ItemList(getCost(), ",", ":");
        }
    }

    public List<Items> getCostItems() {
        return costItems;
    }
}
