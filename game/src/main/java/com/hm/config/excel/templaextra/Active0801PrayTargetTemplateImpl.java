package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0801PrayTargetTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_0801_pray_target")
public class Active0801PrayTargetTemplateImpl extends Active0801PrayTargetTemplate {
	private List<Items> itemList = Lists.newArrayList();

    public void init() {
        this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getItemList() {
        return itemList;
    }
}
