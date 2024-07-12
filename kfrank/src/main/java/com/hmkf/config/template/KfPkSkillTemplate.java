package com.hmkf.config.template;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_pk_skill")
public class KfPkSkillTemplate {
    private Integer id;
    private String tank_limit;
    private Integer skill_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTank_limit() {
        return tank_limit;
    }

    public void setTank_limit(String tank_limit) {
        this.tank_limit = tank_limit;
    }

    public Integer getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(Integer skill_id) {
        this.skill_id = skill_id;
    }
}
