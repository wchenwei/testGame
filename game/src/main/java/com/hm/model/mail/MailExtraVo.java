package com.hm.model.mail;


import com.hm.enums.MailSubType;

public class MailExtraVo {
    private int type;

    public MailExtraVo() {}
    public MailExtraVo(MailSubType type) {
        super();
        this.type = type.getCode();
    }

    public int getType(){
        return type;
    }
}
