package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("central_control_skill_level")
public class CentralControlSkillLevelTemplate {
    private Integer id;
    private Integer type;
    private Integer level;
    private Integer skill_id;
    private Integer next_skill_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(Integer skill_id) {
        this.skill_id = skill_id;
    }

    public Integer getNext_skill_id() {
        return next_skill_id;
    }

    public void setNext_skill_id(Integer next_skill_id) {
        this.next_skill_id = next_skill_id;
    }
}
