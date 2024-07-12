package com.hm.enums;

import java.util.Arrays;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 解锁类型
 * @date 2024/6/4 14:37
 */
public enum UnlockType {
    Function(1, "功能id解锁"),
    FB(2, "副本"),
    MLv(3, "军衔等级"),

    ;

    private int code;
    private String desc;

    private UnlockType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }

    public static UnlockType id2Type(int id) {
        return Arrays.stream(UnlockType.values()).filter(trumpType -> trumpType.getCode() == id).findAny().orElse(null);
    }
}
