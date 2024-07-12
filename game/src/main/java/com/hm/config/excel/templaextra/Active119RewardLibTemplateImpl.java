package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active119RewardLibraryTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Data;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/8/11 9:14
 */
@FileConfig("active_119_reward_library")
@Data
public class Active119RewardLibTemplateImpl extends Active119RewardLibraryTemplate {
    List<Items> rewardList = Lists.newArrayList();

    public void init() {
        rewardList = ItemUtils.str2ItemList(this.getReward(), ",", ":");
    }

    public boolean isFit(int lv){
        return lv>= this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
    }
}
