package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active11TicketTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-10-15
 */
@FileConfig("active_11_ticket")
public class Active11TicketTemplateImpl extends Active11TicketTemplate {
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
