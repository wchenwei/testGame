package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.SmallGameLevelTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @ClassName SmallGameLevelTemplateImpl
 * @Deacription 等级奖励配置
 * @Author zxj
 * @Date 2022/2/14 14:44
 * @Version 1.0
 **/
@FileConfig("small_game_level")
public class SmallGameLevelTemplateImpl extends SmallGameLevelTemplate {
    private List<Items> rewardItems = Lists.newArrayList();

    public void init(){
        this.rewardItems = ItemUtils.str2ItemList(getReward_show(), ",", ":");
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }

    public boolean checkExpScore(long exp, long score) {
        return this.getMaxexp()>=exp && this.getMaxpoint()>=score;
    }

    public List<Items> getReward() {
        return rewardItems;
    }
}
