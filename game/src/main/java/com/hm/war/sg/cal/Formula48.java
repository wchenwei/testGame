package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

/**
 * 目标防御力*系数A*场上存活队友数量
 */
public class Formula48 extends BaseCalFormula {
    private boolean isAtk;
    private TankAttrType attrType;

    public Formula48(boolean isAtk, TankAttrType attrType) {
        this.isAtk = isAtk;
        this.attrType = attrType;
    }

    @Override
    public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
        Unit unit = isAtk ? atk : def;
        int defTroopNum = unit.getMyGroup().getLifeUnit().size() - 1;
        if (defTroopNum <= 1) {
            return 0;
        }
        long attrVal = unit.getUnitAttr().getLongValue(attrType);
        double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
        double skillHurt = attrVal * defTroopNum * p1;
        return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
    }
}
