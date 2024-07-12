package com.hm.enums;

import java.util.Arrays;


public enum GroupGiftType {
    None(0, "error!!!"),
    Day(1, "日礼包"),
    Week(2, "周礼包"),
    Month(3, "月礼包"),;

    public static GroupGiftType num2enum(int num) {
        return Arrays.stream(GroupGiftType.values()).filter(r -> r.getType() == num).findFirst().orElse(None);
    }

    GroupGiftType(int type, String desc) {
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
