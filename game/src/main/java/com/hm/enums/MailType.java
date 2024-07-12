package com.hm.enums;


/**
 * 邮件类型
 *
 * @author yl
 * @version 2013-3-2
 */
public enum MailType {
    SYSTEM(0, "系统邮件"),
    GM(1, "后台邮件"),
    ;

    MailType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;


    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MailType getMailType(int type) {
        for (MailType kind : MailType.values()) {
            if (type == kind.getType()) return kind;
        }
        return null;
    }
}
