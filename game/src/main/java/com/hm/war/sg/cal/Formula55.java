package com.hm.war.sg.cal;

import com.hm.war.sg.SettingManager;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula55 extends BaseCalFormula {
    private int type;

    public Formula55(int type) {
        this.type = type;
    }

    @Override
    public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
        return SettingManager.MSkillHurts[type][skill.getLv()];
    }
}
