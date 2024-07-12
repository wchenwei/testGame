package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active771RechargeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName Active771RechargeTemplateImpl
 * @Author zxj
 * @Date 2022/1/18 12:51
 * @Version 1.0
 **/
@FileConfig("active_771_recharge")
public class Active771RechargeTemplateImpl extends Active771RechargeTemplate {
    //每一个行，有五个奖励，按1，2，3，4，5加入到此map中
    Map<Integer, List<Items>> rewards = Maps.newHashMap();
    //每一个行，有五个解锁的金砖数量，按1，2，3，4，5加入到此map中
    Map<Integer, Integer> rewardGold = Maps.newHashMap();

    public void init(){
        List<Items> rewards1 = ItemUtils.str2ItemList(this.getReward_recharge_1(), ",", ":");
        List<Items> rewards2 = ItemUtils.str2ItemList(this.getReward_recharge_2(), ",", ":");
        List<Items> rewards3 = ItemUtils.str2ItemList(this.getReward_recharge_3(), ",", ":");
        List<Items> rewards4 = ItemUtils.str2ItemList(this.getReward_recharge_4(), ",", ":");
        List<Items> rewards5 = ItemUtils.str2ItemList(this.getReward_recharge_5(), ",", ":");
        rewards.put(1, rewards1);
        rewards.put(2, rewards2);
        rewards.put(3, rewards3);
        rewards.put(4, rewards4);
        rewards.put(5, rewards5);

        rewardGold.put(1, this.getRecharge_gold_1());
        rewardGold.put(2, this.getRecharge_gold_2());
        rewardGold.put(3, this.getRecharge_gold_3());
        rewardGold.put(4, this.getRecharge_gold_4());
        rewardGold.put(5, this.getRecharge_gold_5());
    }

    public boolean fixLv(int lv) {
        return lv>= this.getLv_down() && lv<= this.getLv_up();
    }
    public boolean fixRecharge(int count, int index) {
        return count>=this.rewardGold.get(index);
    }

    public boolean fixLvStageRec(int stage, int lv, int count, int index) {
        return this.getStage()==stage && this.fixLv(lv) && this.fixRecharge(count, index);
    }

    /**
     * 获取未领取的，奖励ids
     * @param count     充值记录
     * @param receive   已经领取的记录
     * @return
     */
    public List<Integer> getFixIds(int count, List<Integer> receive) {
        List<Integer> result = Lists.newArrayList();
        rewards.forEach((key, value)->{
            if((CollectionUtil.isEmpty(receive) ||!receive.contains(key)) && fixRecharge(count, key)) {
                result.add(key);
            }
        });
        return result;
    }

    public List<Items> getReward(int index) {
        return rewards.get(index);
    }

    public List<Items> getRewardAll(List<Integer> value) {
        List<Items> result = Lists.newArrayList();
        value.forEach(e->{
            result.addAll(this.getReward(e));
        });
        return result;
    }
}
