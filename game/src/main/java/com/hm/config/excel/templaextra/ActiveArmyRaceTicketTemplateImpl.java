package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveArmyRaceTicketTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;

/**
 * @description: 军备竞赛 入场券对应订单类型、星级的随机权重
 * @author: chenwei
 * @create: 2020-03-03 11:24
 **/
@FileConfig("active_army_race_ticket")
public class ActiveArmyRaceTicketTemplateImpl extends ActiveArmyRaceTicketTemplate {

    private List<Items> itemNeed = Lists.newArrayList();
    private Map<Integer,Integer> randomWeightMap = Maps.newConcurrentMap();
    public void init(){
        try {
            this.itemNeed = ItemUtils.str2ItemList(this.getItem_need(), ",",":");

            Map<Integer,Integer> tempMap = Maps.newConcurrentMap();
            String star_random = getStar_random();
            for (String tenderStr : StrUtil.split(star_random,",")){
                String [] tender = tenderStr.split(":");
                tempMap.put(Integer.parseInt(tender[0]),Integer.parseInt(tender[1]));
            }
            randomWeightMap = tempMap;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Integer> getRandomWeightMap() {
        return randomWeightMap;
    }

    public List<Items> getItemNeed() {
        return itemNeed;
    }
}
