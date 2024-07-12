package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveYearProgressTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 *          元旦累计次数奖励
 * @date 2020/12/8 13:33
 */
@FileConfig("active_year_progress")
public class ActiveYearProgressTemplateImpl extends ActiveYearProgressTemplate {

    List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }
    public List<Items> getRewards() {
        return rewards;
    }
    public boolean isFit(int playerLv) {
        return playerLv>=this.getPlayer_lv_down() && playerLv<=this.getPlayer_lv_up();
    }
}
