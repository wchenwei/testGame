package com.hm.enums;

/**
 * @Author chenwei
 * @Date 2024/5/20
 * @Description: 十连抽池子类型
 */
public enum LuckPondType {
    Normal(1,"普通池子"),
    TenMust(2,"十次必出"),
    ;

    LuckPondType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;

    public int getType() {
        return type;
    }
}
