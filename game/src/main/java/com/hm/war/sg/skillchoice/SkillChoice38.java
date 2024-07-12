package com.hm.war.sg.skillchoice;

import com.hm.util.RandomUtils;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 除了前排的其它随机几名友军单位#人数
 */
public class SkillChoice38 extends BaseSkillChoice {
    private int num;//随机个数
    private boolean isAtk;

    public SkillChoice38(boolean isAtk) {
        super();
        this.isAtk = isAtk;
    }

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        List<Unit> unitList = isAtk ? atkGroup.getLifeUnit() : defGroup.getLifeUnit();
        List<Unit> noList = ChoiceUtils.getFirstRowUnit(unitList);//最前排
        unitList.removeAll(noList);
        return RandomUtils.randomEleList(unitList, this.num);
    }

    @Override
    public void loadParm(String parm) {
        this.num = Integer.parseInt(parm);
    }
}
