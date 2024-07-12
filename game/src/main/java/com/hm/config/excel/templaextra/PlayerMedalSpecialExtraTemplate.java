package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerMedalSpecialTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-05-12 17:56
 **/
@FileConfig("player_medal_special")
public class PlayerMedalSpecialExtraTemplate extends PlayerMedalSpecialTemplate {
    private List<Items> costItems = Lists.newArrayList();

    public void init(){
        if (StringUtils.isNotBlank(getCost())){
            costItems = ItemUtils.str2ItemList(getCost(),",",":");
        }
    }

    public List<Items> getCostItems(){
        return costItems;
    }
}
