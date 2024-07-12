package com.hm.enums;

/**
 * Description:首都争夺战坦克状态
 * User: yang xb
 * Date: 2018-06-26
 */
public enum BattleStatus {
    Normal(0, "空闲"),
    War(1, "战斗中"),
    Repair(2, "维修状态"),;

    private int type;
    private String desc;

    BattleStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
