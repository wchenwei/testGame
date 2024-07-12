package com.hm.enums;

/**
 * 力量系统 格子数量
 */
public enum StrengthGridType {
    two(2),
    three(3),
    four(4),
    five(5),
    ;

    private StrengthGridType(int type) {
        this.type = type;
    }

    private int type;


    public int getType() {
        return type;
    }
}
