package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CampPrisonerTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-07-07 09:54
 **/
@FileConfig("camp_prisoner")
public class CampPrisonerTemplateImpl extends CampPrisonerTemplate {
    private List<Items> upgradeCostItem = Lists.newArrayList();
    private List<Items> extraRewardItem = Lists.newArrayList();

    public void init(){
        if (StrUtil.isNotEmpty(getUpgrade_cost())){
            this.upgradeCostItem = ItemUtils.str2DefaultItemList(getUpgrade_cost());
        }
        if (StrUtil.isNotEmpty(getExtra_reward())){
            this.extraRewardItem = ItemUtils.str2DefaultItemList(getExtra_reward());
        }
    }

    public List<Items> getUpgradeCostItem() {
        return upgradeCostItem;
    }

    public List<Items> getExtraRewardItem() {
        return extraRewardItem;
    }
}
