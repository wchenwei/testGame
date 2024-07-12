package com.hm.libcore.language;

/**
 * 语言类型
 * TODO 跟ContentType一样，找时间改成同一个
 * @Author ljfeng
 * @Date 2022-09-19
 **/
public enum LanConfType {
    NORMAL(0),//正常
    CONFIG(1),//多语言配置
    GRID(2),//坐标
    ITEM(3),//道具（type,id,count）
    NUMBER(4),//数字转中文
    ;

    /**
     * @param type
     */
    private LanConfType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }
}
