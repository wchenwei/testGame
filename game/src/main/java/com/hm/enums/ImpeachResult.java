package com.hm.enums;

public enum ImpeachResult {
    Error(0,"系统异常"),
    End(1,"已经结束"),
    Normal(2,"进行中"),
    Fail(10,"失败"),
    Succ(11,"成功"),
    ;
    private ImpeachResult(int type,String desc){
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
}
