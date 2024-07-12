package com.hm.war.sg.skillchoice;

import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 敌方攻击最高的n个单位39#2
 */
public class SkillChoice40 extends BaseSkillChoice {
    private boolean isAtk;


    public SkillChoice40(boolean isAtk) {
        this.isAtk = isAtk;
    }

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        List<Unit> unitList = isAtk ? atkGroup.getLifeUnit() : defGroup.getLifeUnit();
        int sendId = atk.getId();
        return unitList.stream().filter(e -> haveBuff(e, sendId))
                .collect(Collectors.toList());
    }

    public boolean haveBuff(Unit unit, int sendId) {
        return unit.getUnitBuffs().getBuffList(UnitBufferType.MarkBuff)
                .stream().anyMatch(e -> e.getBuffSendId() == sendId);
    }

}
