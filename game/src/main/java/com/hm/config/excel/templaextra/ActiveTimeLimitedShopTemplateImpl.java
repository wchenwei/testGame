package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveTimeLimitedShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

/**
 * Description:
 * User: yang xb
 * Date: 2019-08-22
 */
@FileConfig("active_time_limited_shop")
public class ActiveTimeLimitedShopTemplateImpl extends ActiveTimeLimitedShopTemplate {
    private Items goodsItems;
    private Items priceItems;

    public void init() {
        goodsItems = ItemUtils.str2Item(getGoods(), ":");
        priceItems = ItemUtils.str2Item(getPrice(), ":");
    }

    public Items getGoodsItems() {
        return goodsItems;
    }

    public Items getPriceItems() {
        return priceItems;
    }
}
