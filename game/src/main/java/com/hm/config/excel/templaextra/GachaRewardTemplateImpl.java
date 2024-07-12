package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GachaRewardTemplate;
import com.hm.model.item.WeightItem;
import com.hm.util.ItemUtils;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-18
 */
@FileConfig("gacha_reward")
public class GachaRewardTemplateImpl extends GachaRewardTemplate {
    private WeightItem normalItems;
    private WeightItem onceNotGetItems;
    private WeightItem onceGetTankItems;
    private WeightItem tenNotGetItems;
    private WeightItem tenGetTankItems;
    private WeightItem rateTankItems;
    private WeightItem sRateTankItems;

    public void init() {
        normalItems = ItemUtils.str2WeightItem(getNomal(), ":");
        onceNotGetItems = ItemUtils.str2WeightItem(getAdv_once_notget(), ":");
        onceGetTankItems = ItemUtils.str2WeightItem(getAdv_once_gettank(), ":");
        tenNotGetItems = ItemUtils.str2WeightItem(getAdv_ten_notget(), ":");
        tenGetTankItems = ItemUtils.str2WeightItem(getAdv_ten_gettank(), ":");
        rateTankItems = ItemUtils.str2WeightItem(getSs_tank_rate(), ":");
        sRateTankItems = ItemUtils.str2WeightItem(getS_tank_rate(), ":");
    }

    public WeightItem getNormalItems() {
        return normalItems;
    }

    public WeightItem getOnceNotGetItems() {
        return onceNotGetItems;
    }

    public WeightItem getOnceGetTankItems() {
        return onceGetTankItems;
    }

    public WeightItem getTenNotGetItems() {
        return tenNotGetItems;
    }

    public WeightItem getTenGetTankItems() {
        return tenGetTankItems;
    }

    public WeightItem getRateTankItems() {
        return rateTankItems;
    }

    public WeightItem getsRateTankItems() {
        return sRateTankItems;
    }
}
