package com.hm.config.excel.templaextra;

import com.hm.config.excel.temlate.LuckyPondTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

@Getter
@FileConfig("lucky_pond")
public class LuckyPondTemplateImpl extends LuckyPondTemplate {

    private Items rewards;

    @ConfigInit
    public void init(){
        this.rewards = ItemUtils.str2Item(getReward(), ":");
    }

}
