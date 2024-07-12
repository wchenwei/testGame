package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0yuan_gear")
public class Active0yuanGearTemplate {
    private Integer id;
    private String name;
    private Integer Gear;
    private Integer gold_pay;
    private Integer num_limit;
    private Integer back_percent;
    private Integer mail_id;
    private Integer buy_limit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGear() {
        return Gear;
    }

    public void setGear(Integer Gear) {
        this.Gear = Gear;
    }

    public Integer getGold_pay() {
        return gold_pay;
    }

    public void setGold_pay(Integer gold_pay) {
        this.gold_pay = gold_pay;
    }

    public Integer getNum_limit() {
        return num_limit;
    }

    public void setNum_limit(Integer num_limit) {
        this.num_limit = num_limit;
    }

    public Integer getBack_percent() {
        return back_percent;
    }

    public void setBack_percent(Integer back_percent) {
        this.back_percent = back_percent;
    }

    public Integer getMail_id() {
        return mail_id;
    }

    public void setMail_id(Integer mail_id) {
        this.mail_id = mail_id;
    }

    public Integer getBuy_limit() {
        return buy_limit;
    }

    public void setBuy_limit(Integer buy_limit) {
        this.buy_limit = buy_limit;
    }
}
