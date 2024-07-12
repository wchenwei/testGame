package com.hm.enums;

/**
 * @description
 *          航母出击  祈福 buff 类型
 * @date 2021/2/6 11:42
 */
public enum UnchartedType {
    REWARD(1,"探索获得海盗宝藏"),
    NO_CONSUME(2,"探索获得下次航行不消耗军费"),
    DOUBLE_CONSUME(4,"探索获得下次航行军费翻倍"),
    WIN_REWARD(3,"击败海盗获得奖励翻倍"),
    ;

    private UnchartedType(int type,String desc){
        this.type = type;
        this.desc = desc;
    }
    private int type;
    private String desc;

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
}
