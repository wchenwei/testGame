package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MilitaryLineupLevelTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@FileConfig("military_lineup_level")
public class MilitaryLineupLevelTemplateImpl extends MilitaryLineupLevelTemplate {
    private List<Items> costItems = Lists.newArrayList();

    public void init() {
        costItems = ItemUtils.str2ItemList(getCost(), ",", ":");
    }

    public List<Items> getCostItems() {
        return costItems;
    }
}
