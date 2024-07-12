package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active51StageTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-12-09 10:06
 **/
@FileConfig("active_51_stage")
public class Active51StageTemplateImpl extends Active51StageTemplate {

    private List<Items> baseItems = Lists.newArrayList();
    private Items item1;
    private Items item2;
    private Items item3;

    public void init(){
        baseItems = ItemUtils.str2DefaultItemList(getBase_item());
        item1 = ItemUtils.str2Item(getItem_1(),":");
        item2 = ItemUtils.str2Item(getItem_2(),":");
        item3 = ItemUtils.str2Item(getItem_3(),":");
    }

    public List<Items> getBaseItems() {
        return baseItems;
    }

    public Items getItem1() {
        return item1;
    }

    public Items getItem2() {
        return item2;
    }

    public Items getItem3() {
        return item3;
    }

    public Items getWeaponItem(int type){
        switch (type){
            case 1:
                return item1;
            case 2:
                return item2;
            case 3:
                return item3;
        }
        return null;
    }
}
