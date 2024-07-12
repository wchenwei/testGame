package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveConsumeTowerTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 消费宝塔
 * @author: chenwei
 * @create: 2020-03-13 15:25
 **/
@FileConfig("active_consume_tower")
public class ActiveConsumeTowerTemplateImpl extends ActiveConsumeTowerTemplate {
    private List<Items> rewards = Lists.newArrayList();

    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",",":");
    }

    public List<Items> getRewards() {
        return rewards;
    }

    public boolean isFit(int lv) {
        return lv>=getLv_down()&&lv<=getLv_up();
    }

    /**
     * 是否稀有
     * @return
     */
    public boolean isUnusual(){
        return getCircle_effect() == 1;
    }
}
