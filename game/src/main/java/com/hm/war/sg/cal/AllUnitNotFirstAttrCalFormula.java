package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 除了前排外的队友抗暴*系数A+系数B
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/3/25 10:57
 */
public class AllUnitNotFirstAttrCalFormula extends BaseCalFormula {
    private TankAttrType attrType;//属性类型
    private boolean isAtk;

    public AllUnitNotFirstAttrCalFormula(TankAttrType attrType, boolean isAtk) {
        this.attrType = attrType;
        this.isAtk = isAtk;
    }

    @Override
    public double calFormula(Unit atk, Unit def, Skill skill, SkillFunction skillFunction, Object... args) {
        List<Unit> unitList = isAtk ? atk.getMyGroup().getLifeUnit() : atk.getDefGroup().getLifeUnit();
        List<Unit> noList = ChoiceUtils.getFirstRowUnit(unitList);//最前排
        unitList.removeAll(noList);

        double totalVal = unitList.stream().mapToDouble(e -> e.getUnitAttr().getDoubleValue(attrType)).sum();
        double p1 = skillFunction.getParmList().get(0).getLvValue(skillFunction.getLv());
        double p2 = skillFunction.getParmList().get(1).getLvValue(skillFunction.getLv());
        double skillHurt = totalVal * p1 + p2;
        return Math.max(0, skillFunction.calFormula(atk, def, skillHurt));
    }
}
