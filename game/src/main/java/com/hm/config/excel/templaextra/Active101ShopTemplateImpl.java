package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active101ShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

/**
 * Description:
 * User: yang xb
 * Date: 2019-08-09
 */
@FileConfig("active_101_shop")
public class Active101ShopTemplateImpl extends Active101ShopTemplate {
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
