package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class Formula54 extends BaseCalFormula {

    @Override
    public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
        long atkValue = def.getUnitAttr().getLongValue(TankAttrType.ATK);
        long defValue = def.getUnitAttr().getLongValue(TankAttrType.DEF);

        double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
        double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());

        int lineId = ChoiceUtils.getLine(def.getIndex());
        int num = (int)def.getMyGroup().getLifeUnit().stream().filter(e -> ChoiceUtils.getLine(e.getIndex()) == lineId)
                .count();

        double skillHurt = ((atkValue-defValue)*p1+p2)*num;
        return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
    }


}
