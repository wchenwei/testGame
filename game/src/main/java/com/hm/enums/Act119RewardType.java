package com.hm.enums;

/**
 * @author wyp
 * @description
 * @date 2022/8/11 9:16
 */
public enum Act119RewardType {
    Common(1, "普通奖励"),
    specialLock(2, "解锁奖励"),
    ;

    private int code;
    private String desc;

    private Act119RewardType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
