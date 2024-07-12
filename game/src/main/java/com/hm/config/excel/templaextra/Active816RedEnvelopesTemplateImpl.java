package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active816RedEnvelopesTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_816_red_envelopes")
public class Active816RedEnvelopesTemplateImpl extends Active816RedEnvelopesTemplate {
    private List<Items> goodsItems;

    public void init() {
        goodsItems = ItemUtils.str2DefaultItemImmutableList(getGoods());
    }

    public List<Items> getGoodsItems() {
        return goodsItems;
    }
}
