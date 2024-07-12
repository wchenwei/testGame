package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CampPrisonerResearcherTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-07-07 09:55
 **/
@FileConfig("camp_prisoner_researcher")
public class CampPrisonerResearcherTemplateImpl extends CampPrisonerResearcherTemplate {
    private List<Items> hirePriceCost = Lists.newArrayList();
    private List<WeightMeta<Items>> extraDrop = Lists.newArrayList();

    public void init(){
        if (StrUtil.isNotEmpty(getHire_price())){
            this.hirePriceCost = ItemUtils.str2DefaultItemList(getHire_price());
        }
        if (StrUtil.isNotEmpty(getExtra_drop())){
            for (String drops : getExtra_drop().split(";")) {
                this.extraDrop.add(RandomUtils.buildItemWeightMeta(drops));
            }
        }
    }

    public List<Items> getHirePriceCost() {
        return hirePriceCost;
    }

    public List<Items> getExtraDropItems(){
        List<Items> dropItems = Lists.newArrayList();
        if (CollUtil.isNotEmpty(extraDrop)){
            extraDrop.forEach(e->{
                Items random = e.random();
                if (random != null && random.getItemType()>0){
                    dropItems.add(random);
                }
            });
        }
        return dropItems;
    }
}
