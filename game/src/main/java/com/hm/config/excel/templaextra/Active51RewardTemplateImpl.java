package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.Active51RewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-04-14 19:18
 **/
@FileConfig("active_51_reward")
public class Active51RewardTemplateImpl extends Active51RewardTemplate {
    private List<WeightMeta<Items>> weightMetahtList = Lists.newArrayList();

    public void init(){
        for (String rewards : StringUtil.splitStr2StrList(getReward(),";")){
            this.weightMetahtList.add(RandomUtils.buildItemWeightMeta(rewards));
        }
    }

    public List<Items> getRewards() {
        List<Items> rewards = Lists.newArrayList();
        for (WeightMeta<Items> weightMeta : weightMetahtList){
            rewards.add(weightMeta.random());
        }
        return rewards;
    }
    public boolean isFit(int lv) {
        return lv>=getLv_down()&&lv<=getLv_up();
    }
}
