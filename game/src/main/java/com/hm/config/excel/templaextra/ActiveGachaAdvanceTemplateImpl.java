package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveGachaAdvanceTemplate;
import com.hm.model.item.WeightItem;
import com.hm.util.ItemUtils;

@FileConfig("active_gacha_advance")
public class ActiveGachaAdvanceTemplateImpl extends ActiveGachaAdvanceTemplate {
    private WeightItem normalItems;
    private WeightItem onceNotGetItems;
    private WeightItem onceGetTankItems;
    private WeightItem tenNotGetItems;
    private WeightItem tenGetTankItems;
    private WeightItem rateTankItems;
    private WeightItem sRateTankItems;
    private WeightItem tenGetPaper;

    public void init() {
        normalItems = ItemUtils.str2WeightItem(getNomal(), ":");
        onceNotGetItems = ItemUtils.str2WeightItem(getAdv_once_notget(), ":");
        onceGetTankItems = ItemUtils.str2WeightItem(getAdv_once_gettank(), ":");
        tenNotGetItems = ItemUtils.str2WeightItem(getAdv_ten_notget(), ":");
        tenGetTankItems = ItemUtils.str2WeightItem(getAdv_ten_gettank(), ":");
        rateTankItems = ItemUtils.str2WeightItem(getSs_tank_rate(), ":");
        sRateTankItems = ItemUtils.str2WeightItem(getS_tank_rate(), ":");
        tenGetPaper = ItemUtils.str2WeightItem(getAdv_ten_getpaper(), ":");
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

    public WeightItem getTenGetPaper() {
        return tenGetPaper;
    }
}
