package com.hm.enums;

/**
 *
 * @Description: 假日庆典商店
 * @author chenwei
 * @date 2020年2月11日 下午1:30:57
 * @version V1.0
 */
public enum HolidayShopType {

    OrangeShop(8002,"橙装商店"),
    RedShop(8003,"红装商店"),
    UpRedShop(8004,"红装进阶商店"),
    ;

    HolidayShopType(int shopId, String desc) {
        this.shopId = shopId;
        this.desc = desc;
    }

    private int shopId;
    private String desc;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
