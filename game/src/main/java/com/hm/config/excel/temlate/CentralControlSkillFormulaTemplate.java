package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("central_control_skill_formula")
public class CentralControlSkillFormulaTemplate {
    private Integer id;
    private Integer type;
    private String formula_position;
    private String formula_no_position;
    private Integer skill_type;
    private Integer skill_id;

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

    public String getFormula_position() {
        return formula_position;
    }

    public void setFormula_position(String formula_position) {
        this.formula_position = formula_position;
    }

    public String getFormula_no_position() {
        return formula_no_position;
    }

    public void setFormula_no_position(String formula_no_position) {
        this.formula_no_position = formula_no_position;
    }

    public Integer getSkill_type() {
        return skill_type;
    }

    public void setSkill_type(Integer skill_type) {
        this.skill_type = skill_type;
    }

    public Integer getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(Integer skill_id) {
        this.skill_id = skill_id;
    }
}
