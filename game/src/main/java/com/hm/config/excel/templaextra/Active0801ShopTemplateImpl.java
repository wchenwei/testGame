package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0801ShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

@FileConfig("active_0801_shop")
public class Active0801ShopTemplateImpl extends Active0801ShopTemplate {
    private Items goodsItems;
    private Items priceItems;

    public void init() {
        this.goodsItems = ItemUtils.str2Item(this.getGoods(), ":");
        this.priceItems = ItemUtils.str2Item(this.getPrice(), ":");
    }

    public Items getGoodsItems() {
        return goodsItems;
    }

    public Items getPriceItems() {
        return priceItems;
    }
}
