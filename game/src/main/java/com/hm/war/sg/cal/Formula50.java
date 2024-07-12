package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

/**
 * （己方存活战车攻击总和*系数A+系数B）*（最低0.1（1-敌军飞机减伤））
 */
public class Formula50 extends BaseCalFormula {


    @Override
    public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
        int size = atk.getMyGroup().getLifeUnit().size();
        double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
        double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());

        double reduceVal = def.getUnitAttr().getDoubleValue(TankAttrType.AircraftReduce);
        return Math.max(0, (size * p1 + p2) * (Math.max(1 - reduceVal, 0.1)));
    }
}
