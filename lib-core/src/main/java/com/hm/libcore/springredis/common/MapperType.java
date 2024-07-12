package com.hm.libcore.springredis.common;

/**
 * @description: 映射的存储类型
 * @author: chenwei
 * @create: 2020-09-25 14:23
 **/
public enum MapperType {
    /**
     * 单个对象存储为一个hash
     * key : gameDbname + 对象类名 + serverId + primaryKey
     * filed : 属性名
     * value : 属性值
     * 例  Camp 阵营
     * key:    tank4_game_Camp_2_2
     * filed:  campLevel、campBaseInfo等
     * value:  camp的属性值
     */
    HASH_JSON(1, "hash json 单个对象"),

    /**
     * 相同的对象存储为一个hash
     * key: gameDbname + 对象类名 + serverId
     * filed: primaryKey
     * value: 对象的值
     * 例： Camp 阵营
     * key:tank4_game_Camp_2
     * filed: 1、2、3
     * value: camp值
     */
    STRING_HASH(2, "hash 同一类对象"),

    /**
     * 单个对象存储为一个String
     * key : gameDbname + 对象类名 + serverId + primaryKey
     * value : 对象值
     * 例  Camp 阵营
     * key:    tank4_game_Camp_2_2
     * value:  camp值
     */
    STRING_VALUE(3, "字符串"),
    ;


    private int type;
    private String desc;

    MapperType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }
}
