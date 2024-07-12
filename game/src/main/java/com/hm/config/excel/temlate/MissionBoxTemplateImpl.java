package com.hm.config.excel.temlate;

import com.hm.config.GameConstants;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import lombok.Data;

@Data
@FileConfig("mission_box")
public class MissionBoxTemplateImpl extends MissionBoxTemplate{
    private int type;
    private int itemId;
    private long count;

    private long timeCd;

    @ConfigInit
    public void init() {
        Items items = ItemUtils.str2Item(getReward(),":");
        this.type = items.getType();
        this.itemId = items.getId();
        this.count = items.getCount();
        this.timeCd = getCd()* GameConstants.MINUTE;
    }

    public double calItemCount(long time) {
        return MathUtils.div(time*count, timeCd,2);
    }
}
