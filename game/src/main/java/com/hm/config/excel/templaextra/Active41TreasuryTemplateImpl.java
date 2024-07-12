package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active41TreasuryTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/3/21 14:49
 */
@FileConfig("active_41_treasury")
public class Active41TreasuryTemplateImpl extends Active41TreasuryTemplate {
    private List<Items> itemsList;
    private List<Items> itemsBigList;
    private List<Items> consumeList;

    public void init() {
        this.itemsList = ItemUtils.str2DefaultItemImmutableList(getBox_reward());
        this.consumeList = ItemUtils.str2DefaultItemImmutableList(getConsume());
        if (StrUtil.isNotBlank(getBigbox_reward())) {
            this.itemsBigList = ItemUtils.str2DefaultItemImmutableList(getBigbox_reward());
        }
    }

    public List<Items> getItemsList() {
        return itemsList;
    }

    public List<Items> getItemsBigList() {
        return itemsBigList;
    }

    public List<Items> getConsumeList() {
        return consumeList;
    }

    public boolean isFit(int lv) {
        return lv >= getLv_down() && lv <= getLv_up();
    }
}
