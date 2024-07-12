package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 找最多坦克的一排
 */
public class SkillChoice43 extends BaseSkillChoice {
    private boolean isAtk;


    public SkillChoice43(boolean isAtk) {
        this.isAtk = isAtk;
    }

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        List<Unit> unitList = isAtk ? atkGroup.getLifeUnit() : defGroup.getLifeUnit();
        if(CollUtil.isEmpty(unitList)) {
            return unitList;
        }
        int[] lines = new int[3];
        for (Unit unit : unitList) {
            lines[ChoiceUtils.getLine(unit.getIndex())-1] ++;
        }
        int max = ArrayUtil.max(lines);//最多的人数
        List<Integer> luckList = Lists.newArrayList();
        for (int i = 0; i < lines.length; i++) {
            if(lines[i] == max) {
                luckList.add(i+1);
            }
        }
        int luckLine= RandomUtil.randomEle(luckList);
        return unitList.stream().filter(e -> ChoiceUtils.getLine(e.getIndex()) == luckLine)
                .collect(Collectors.toList());
    }


}
