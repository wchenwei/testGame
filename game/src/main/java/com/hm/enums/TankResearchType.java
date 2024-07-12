package com.hm.enums;

import java.util.Arrays;

/**
 * 十连抽类型
 * @Author chenwei
 * @Date 2024/6/21
 * @Description:
 */
public enum TankResearchType {

    Ad(0, 1,"看广告"),
    One(1, 1,"单抽"),
    Ten(2, 10,"十连抽"),
    ;

    TankResearchType(int type, int count, String desc) {
        this.type = type;
        this.count = count;
        this.desc = desc;
    }

    private int type;
    private int count;
    private String desc;

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public static TankResearchType getTankResearchType(int type){
        return Arrays.stream(TankResearchType.values()).filter(e -> e.getType() == type).findFirst().orElse(null);
    }
}
