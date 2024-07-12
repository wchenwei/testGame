package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.KfPeaceShopTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @ClassName KfPeaceShopTemplateImpl
 * @Deacription 跨服世界大战商店
 * @Author zxj
 * @Date 2021/11/26 16:11
 * @Version 1.0
 **/
@FileConfig("kf_peace_shop")
public class KfPeaceShopTemplateImpl extends KfPeaceShopTemplate {
    private List<Items> rewards = Lists.newArrayList();
    private List<Items> prices = Lists.newArrayList();
    public void init() {
        this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
        this.prices = ItemUtils.str2ItemList(this.getPrice(), ",", ":");
    }
    public List<Items> getRewards() {
        return rewards;
    }
    public List<Items> getPrices() {
        return prices;
    }

    public boolean isFit(int serverLv) {
        return serverLv>=this.getServer_lv_down()&&serverLv<=this.getServer_lv_up();
    }
}
