package com.hm.enums;

import java.util.Arrays;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-18
 */
// gacha_config.xlsx.type col
public enum GachaType {
    None(0, "error!!!"),
    Junior(1, "普通抽奖"),
    SeniorSingle(2, "高级抽奖单抽"),
    SeniorMulNone(3, "高级抽奖十连抽"),;

    public static GachaType num2enum(int num) {
        return Arrays.stream(GachaType.values()).filter(r -> r.getType() == num).findFirst().orElse(None);
    }

    GachaType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private int type;
    private String desc;
}
