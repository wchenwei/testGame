package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveCvoutCircleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/2/5 9:32
 */
@FileConfig("active_cvout_circle")
public class ActiveCvoutCircleTemplateImpl extends ActiveCvoutCircleTemplate {

    private List<Items> rewardItems;

    public void init() {
        rewardItems = ItemUtils.str2ItemList(getReward(), ",", ":");;
    }

    public List<Items>  getRewardItems() {
        return rewardItems;
    }

    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
