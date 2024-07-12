package com.hm.enums;

import java.util.Arrays;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-12
 *
 * @author Administrator
 */
public enum RandomTaskStatus {

    /**
     * test
     */
	NotAccepted(0, "未接受"), //20
    Accept(1, "已接受"),//60
    Finish(2, "完成"),//1-10
    Wait(3,"等待下一事件刷新")
    ;

    private int type;
    private String desc;

    RandomTaskStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static RandomTaskStatus id2Status(int id) {
        return Arrays.stream(RandomTaskStatus.values()).filter(e -> e.getType() == id).findFirst().orElse(null);
    }
}
