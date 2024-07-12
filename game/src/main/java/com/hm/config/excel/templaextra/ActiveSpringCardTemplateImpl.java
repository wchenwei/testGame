package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpringCardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_spring_card")
public class ActiveSpringCardTemplateImpl extends ActiveSpringCardTemplate {
    private List<Items> costItems = Lists.newArrayList();

    public void init() {
        this.costItems = ItemUtils.str2ItemList(getCompose(), ",", ":");
    }

    public List<Items> getCostItems() {
        return costItems;
    }
}
