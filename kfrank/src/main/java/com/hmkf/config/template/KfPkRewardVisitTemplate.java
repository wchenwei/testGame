package com.hmkf.config.template;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_pk_reward_visit")
public class KfPkRewardVisitTemplate {
    private Integer id;
    private Integer rank;
    private Integer sever_lv_down;
    private Integer sever_lv_up;
    private String reward;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getSever_lv_down() {
        return sever_lv_down;
    }

    public void setSever_lv_down(Integer sever_lv_down) {
        this.sever_lv_down = sever_lv_down;
    }

    public Integer getSever_lv_up() {
        return sever_lv_up;
    }

    public void setSever_lv_up(Integer sever_lv_up) {
        this.sever_lv_up = sever_lv_up;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
