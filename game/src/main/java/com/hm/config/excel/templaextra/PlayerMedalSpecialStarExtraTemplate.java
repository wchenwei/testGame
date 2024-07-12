package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerMedalSpecialStarTemplate;
import com.hm.enums.TankAttrType;
import com.hm.enums.TankType;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-05-12 17:55
 **/
@FileConfig("player_medal_special_star")
public class PlayerMedalSpecialStarExtraTemplate extends PlayerMedalSpecialStarTemplate {
    private List<Items> costItems = Lists.newArrayList();
    private Map<Integer,TankAttr> attrMap = Maps.newConcurrentMap();

    public void init(){
        if (StringUtils.isNotBlank(getCost())){
            costItems = ItemUtils.str2ItemList(getCost(),",",":");
        }
    }

    public void reloadAttrMap(List<PlayerMedalSpecialStarExtraTemplate> list){
        list.stream().filter(e -> e.getMedal_lv() <= getMedal_lv()).forEach(template ->{
            if(StringUtils.isNotBlank(template.getAttri())){
                for (TankType tankType : TankType.values()){
                    if (tankType.getType() == template.getTank_type() || template.getTank_type() == -1){
                        Map<TankAttrType, Double> tempMap = TankAttrUtils.str2TankAttrMap(template.getAttri(), ",", ":");
                        TankAttr tankAttr = this.attrMap.getOrDefault(tankType.getType(), new TankAttr());
                        tankAttr.addAttr(tempMap);
                        this.attrMap.put(tankType.getType(),tankAttr);
                    }
                }
            }
        });


    }

    public List<Items> getCostItems(){
        return costItems;
    }

    public Map<Integer,TankAttr> getAttrMap() {
        return attrMap;
    }
}
