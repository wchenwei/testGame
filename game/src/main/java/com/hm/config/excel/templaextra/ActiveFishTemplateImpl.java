package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveFishTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;

import java.util.List;

/**
 * @introduce: 类注释
 * @author: wyp
 * @DATE: 2023/11/1
 **/
@FileConfig("active_fish")
public class ActiveFishTemplateImpl extends ActiveFishTemplate {
    private List<Items> firstReward = Lists.newArrayList();
    // 奖励积分
    private List<Items> integralList = Lists.newArrayList();

    public void init() {
        this.firstReward = ItemUtils.str2ItemList(this.getGold(), ",", ":");
        this.integralList = ItemUtils.str2ItemList(this.getIntegral(), ",", ":");
    }

    public List<Items> getFirstReward() {
        return firstReward;
    }

    public List<Items> getIntegralList() {
        return integralList;
    }

    public int getRandomFishSize(){
        return RandomUtils.randomIntForEnd(this.getMinsize(), this.getMaxsize());
    }
}
