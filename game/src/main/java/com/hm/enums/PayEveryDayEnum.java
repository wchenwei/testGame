package com.hm.enums;


/**
 * ClassName: PayEveryDayEnum. <br/>
 * Function: 每天必买的状态信息. <br/>
 * date: 2019年6月12日 下午8:03:48 <br/>
 *
 * @author zxj
 */
public enum PayEveryDayEnum {

    Normal(0, "未购买"),
    Buy(1, "已购买，可领取"),
    Get(2, "已领取"),
    ;
    int state;
    String desc;

    PayEveryDayEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
