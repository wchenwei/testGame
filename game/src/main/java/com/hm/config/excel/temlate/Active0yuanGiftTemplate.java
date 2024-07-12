package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0yuan_gift")
public class Active0yuanGiftTemplate {
    private Integer id;
    private Integer gift_id;
    private Integer day;
    private String reward_back;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGift_id() {
        return gift_id;
    }

    public void setGift_id(Integer gift_id) {
        this.gift_id = gift_id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getReward_back() {
        return reward_back;
    }

    public void setReward_back(String reward_back) {
        this.reward_back = reward_back;
    }
}
