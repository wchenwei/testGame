package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula52 extends BaseCalFormula {

    @Override
    public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
        long atkValue = atk.getUnitAttr().getLongValue(TankAttrType.ATK);
        int defNum = Math.min(5, atk.getDefGroup().getLifeUnit().size());
        if(defNum <= 0) {
            return 0;
        }
        double p1 = skillFunction.getParmList().get(defNum - 1).getLvValue(skillFunction.getLv());
        double p2 = skillFunction.getParmList().get(5).getLvValue(skillFunction.getLv());

        double skillHurt = atkValue * p1 + p2;

        return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
    }


}
