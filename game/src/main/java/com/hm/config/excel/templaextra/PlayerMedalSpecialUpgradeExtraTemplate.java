package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerMedalSpecialUpgradeTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-05-12 17:57
 **/
@FileConfig("player_medal_special_upgrade")
public class PlayerMedalSpecialUpgradeExtraTemplate extends PlayerMedalSpecialUpgradeTemplate {
    private List<Items> costItems = Lists.newArrayList();
    private Map<TankAttrType, Double> attrMap = Maps.newHashMap();

    public void init(){
        if (StringUtils.isNotBlank(getCost())){
            costItems = ItemUtils.str2ItemList(getCost(),",",":");
        }
        if(StringUtils.isNotBlank(getAttri())){
            this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",",":");
        }
    }

    public List<Items> getCostItems(){
        return costItems;
    }

    public Map<TankAttrType, Double> getAttrMap() {
        return attrMap;
    }
}
