package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active119StageTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Data;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/8/11 9:14
 */
@FileConfig("active_119_stage")
@Data
public class Active119StageTemplateImpl extends Active119StageTemplate {
    List<Items> rewardList = Lists.newArrayList();
    List<Items> costList = Lists.newArrayList();

    public void init() {
        rewardList = ItemUtils.str2ItemList(this.getReward(), ",", ":");
        costList = ItemUtils.str2ItemList(this.getCost(), ",", ":");
    }
}
