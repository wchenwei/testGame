package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveNavyWeaponTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description: 海军节
 * @author: chenwei
 * @create: 2020-03-30 15:04
 **/
@FileConfig("active_navy_weapon")
public class ActiveNavyWeaponTemplateImpl extends ActiveNavyWeaponTemplate {

    private List<Items> costs = Lists.newArrayList();

    public void init(){
        this.costs = ItemUtils.str2ItemList(this.getNeed_item(),",",":");
    }

    public List<Items> getCosts(){
        return costs;
    }

}
