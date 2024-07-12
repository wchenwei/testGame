package com.hm.enums;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-08
 */
public enum BoatState {
    Free(0, "空闲"),
    Ship(1, "航运"),
    Reform(2, "改造"),
    Waiting(3, "等待中")
    ;

    private int type;
    private String desc;

    BoatState(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }}
