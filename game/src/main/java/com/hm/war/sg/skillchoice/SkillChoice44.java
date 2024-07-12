package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.util.RandomUtils;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.Comparator;
import java.util.List;

/**
 * 攻击力最高的和血量最少的目标
 */
public class SkillChoice44 extends BaseSkillChoice {
    private boolean isAtk;

    public SkillChoice44(boolean isAtk) {
        this.isAtk = isAtk;
    }

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        List<Unit> unitList = isAtk ? atkGroup.getLifeUnit() : defGroup.getLifeUnit();
        if(unitList.size() < 2) {
            return Lists.newArrayList();
        }
        //找出攻击力最高的
        Unit atkUnit = unitList.stream().max(Comparator.comparingLong(Unit::getCurAtk)).orElse(null);
        unitList.remove(atkUnit);

        List<Unit> row1List = Lists.newArrayList();//第1排
        List<Unit> row2List = Lists.newArrayList();//后2排

        for (Unit unit : unitList) {
            if(ChoiceUtils.getLine(unit.getIndex()) == 1) {
                row1List.add(unit);
            }else{
                row2List.add(unit);
            }
        }

        Unit hpUnit = null;
        if(CollUtil.isNotEmpty(row2List)) {
            hpUnit = RandomUtils.randomEle(row2List);
        }else{
            hpUnit = RandomUtils.randomEle(row1List);
        }

        if(atkUnit == null || hpUnit == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(atkUnit,hpUnit);
    }



}
