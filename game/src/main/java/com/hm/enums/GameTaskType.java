package com.hm.enums;

/**
 * 活动类型
 * @Author chenwei
 * @Date 2024/7/1
 * @Description:
 */
public enum GameTaskType {
    T1(1,"主线任务"),
    T2(2,"日常任务"),
    T3(3,"活动任务"),
    T4(4,"成就任务"),

    ;

    GameTaskType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;

    public int getType() {
        return type;
    }
}
