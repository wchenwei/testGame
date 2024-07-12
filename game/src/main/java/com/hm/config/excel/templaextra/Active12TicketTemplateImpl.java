package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active12TicketTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-11-21
 */
@FileConfig("active_12_ticket")
public class Active12TicketTemplateImpl extends Active12TicketTemplate {
    private List<Items> itemList = Lists.newArrayList();

    public void init() {
        this.itemList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getItemList() {
        return itemList;
    }

    public Pair<Integer, Integer> genGroupKey() {
        return Pair.of(getSever_lv_down(), getSever_lv_up());
    }
}
