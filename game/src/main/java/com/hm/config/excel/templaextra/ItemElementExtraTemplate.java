package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ItemElementTemplate;
import com.hm.enums.TankAttrType;

import java.util.Map;

@FileConfig("item_element")
public class ItemElementExtraTemplate extends ItemElementTemplate {
    private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
//    //合成消耗
//    private List<Items> composeCost = Lists.newArrayList();
//    //升级消耗
//    private List<Items> lvUpCost = Lists.newArrayList();
    public void init(){
        for(String str:getAttr().split(",")){
            String[] strs = str.split(":");
            TankAttrType  attrType = TankAttrType.getType(Integer.parseInt(strs[0]));
            double value = Double.parseDouble(strs[1]);
            if(attrType!=null&&value>0){
                attrMap.put(attrType, value);
            }
        }
//        this.composeCost = ItemUtils.str2ItemImmutableList(this.getCost(), ",", ":");
//        this.lvUpCost = ItemUtils.str2ItemImmutableList(this.getLevelup_cost(), ",", ":");
    }

//    public List<Items> getComposeCost(long count){
//        return composeCost.stream().map(t-> {
//            Items item = t.clone();
//            item.setCount(item.getCount()*count);
//            return item;
//        }).collect(Collectors.toList());
//    }
//    public List<Items> getLvUpCost(){
//        return getLvUpCost(1);
//    }

//    public List<Items> getLvUpCost(long num){
//        return lvUpCost.stream().map(t -> {
//            Items item = t.clone();
//            item.setCount(item.getCount()*num);
//            return item;
//        }).collect(Collectors.toList());
//    }

    public Map<TankAttrType, Double> getAttrMap() {
        return attrMap;
    }
}
