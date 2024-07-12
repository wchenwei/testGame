package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.hm.util.RandomUtils;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 敌方随机1排#坦克数量
 */
public class SkillChoice42 extends BaseSkillChoice {
    private int num;
    private boolean isAtk;


    public SkillChoice42(boolean isAtk) {
        this.isAtk = isAtk;
    }

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        List<Unit> unitList = isAtk ? atkGroup.getLifeUnit() : defGroup.getLifeUnit();
        if(CollUtil.isEmpty(unitList)) {
            return unitList;
        }
        Map<Integer,List<Unit>> rowMap = unitList.stream().collect(Collectors.groupingBy(
                e -> ChoiceUtils.getLine(e.getIndex())
        ));
        List<Unit> luckList = rowMap.get(RandomUtil.randomEle(Lists.newArrayList(rowMap.keySet())));
        return RandomUtils.randomEleList(luckList,this.num);
    }



    @Override
    public void loadParm(String parm) {
        this.num = Integer.parseInt(parm);
    }
}
