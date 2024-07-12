package com.hm.config.excel.templaextra;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.AgentBaseTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-07-08
 */
@FileConfig("agent_base")
public class AgentBaseTemplateImpl extends AgentBaseTemplate {
    private Map<TankAttrType, Double> tankAttrMap = Maps.newConcurrentMap();
    private List<TankAttrType> tankAttrList = Lists.newArrayList();
    /**
     * pair.first base attr ;second lv added attr
     */
    private Map<TankAttrType, Pair<Double, Double>> totalAttrPairMap = Maps.newConcurrentMap();

    /**
     * 派遣额外奖励
     */
    private List<Items> rewardItems;
    /**
     * 派遣额外奖励活动概率
     */
    private double rate;

    public void init() {
        this.tankAttrMap = TankAttrUtils.str2TankAttrMap(getTank_attr(), ",", ":");
        for(String str :getTank_attr().split(",")) {
            int attrId = Integer.parseInt(str.split(":")[0]);
            tankAttrList.add(TankAttrType.getType(attrId));
        }
        Arrays.stream(getTotal_attr().split(",")).map(str -> str.split(":")).filter(split -> split.length >= 3).forEach(split -> {
            int attrId = Integer.parseInt(split[0]);
            double valueBase = Double.parseDouble(split[1]);
            double valueLv = Double.parseDouble(split[2]);
            totalAttrPairMap.put(TankAttrType.getType(attrId), Pair.of(valueBase, valueLv));
        });

        rewardItems = ItemUtils.str2DefaultItemImmutableList(getItem_drop());;
        rate = Convert.toDouble(getOdds_drop(), 0D);
    }

    public Map<TankAttrType, Double> getTankAttrMap() {
        return tankAttrMap;
    }

    public List<TankAttrType> getTankAttrList() {
        return tankAttrList;
    }

    public Map<TankAttrType, Pair<Double, Double>> getTotalAttrPairMap() {
        return totalAttrPairMap;
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }

    public double getRate() {
        return rate;
    }
}
