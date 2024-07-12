package com.hm.enums;

/**
 * ClassName: ServerTypeEnum. <br/>
 * Function: 表示http请求的结果，成功还是失败. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 *
 * @author zxj
 */
public enum HttpPostStrResult {
    SUCC("succ", "成功"),
    ERROR("error", "失败"),
    ;

    private HttpPostStrResult(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;
    private String desc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}



