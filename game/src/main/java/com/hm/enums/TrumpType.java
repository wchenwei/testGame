package com.hm.enums;

import java.util.Arrays;

/**
 * Description:	王牌演习 五个段位：青铜、白银、黄金、白金、钻石
 * User: yang xb
 * Date: 2019-03-26
 */
public enum TrumpType {
    /**
     * 青铜
     */
    Bronze(1, "青铜"),
    /**
     * 白银
     */
    Silver(2, "白银"),
    /**
     * 黄金
     */
    Gold(3, "黄金"),
    /**
     * 白金
     */
    Platinum(4, "白金"),
    /**
     * 钻石
     */
    Diamond(5, "钻石"),
    ;

    int type;
    String desc;

    TrumpType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static TrumpType id2Type(int id) {
        return Arrays.stream(TrumpType.values()).filter(trumpType -> trumpType.getType() == id).findAny().orElse(null);
    }
}
