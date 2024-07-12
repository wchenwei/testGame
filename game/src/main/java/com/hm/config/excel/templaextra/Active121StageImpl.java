package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active1001StageTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;

@FileConfig("active_1001_stage")
public class Active121StageImpl extends Active1001StageTemplate {
    //消耗
    private List<Items> cost = Lists.newArrayList();
    //奖励
    private List<Items> recharge = Lists.newArrayList();

    private List<Integer> rankTypeList = Lists.newArrayList();

    public void init() {
        this.cost = ItemUtils.str2ItemList(this.getCost_circle(), ",", ":");
        this.recharge = ItemUtils.str2ItemList(this.getReward_recharge_vip(), ",", ":");
        rankTypeList = StringUtil.splitStr2IntegerList(this.getRank_type(), ",");
    }


    public List<Items> getCost() {
        return cost;
    }
    public List<Items> getRecharge() {
        return recharge;
    }
    //根据天数，获取排行榜类型
    public int getRankType(int day) {
        if(rankTypeList.size()>=day) {
            return rankTypeList.get(day-1);
        }
        return -1;
    }
}


