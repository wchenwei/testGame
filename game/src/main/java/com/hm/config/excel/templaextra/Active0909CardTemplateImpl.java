package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0909CardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_0909_card")
public class Active0909CardTemplateImpl extends Active0909CardTemplate {
    private List<Items> costItems = Lists.newArrayList();

    public void init() {
        this.costItems = ItemUtils.str2ItemList(getCompose(), ",", ":");
    }

    public List<Items> getCostItems() {
        return costItems;
    }
}
