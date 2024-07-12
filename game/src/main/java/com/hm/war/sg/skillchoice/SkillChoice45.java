package com.hm.war.sg.skillchoice;

import com.google.common.collect.Lists;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.Map;

/**
 * 上下左右
 */
public class SkillChoice45 extends BaseSkillChoice {

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        Map<Integer,Unit> unitMap = atk.getMyGroup().getLifeUnitMap();
        List<Unit> resultList = Lists.newArrayList();
        for (int nearIndex : ChoiceUtils.NearIndex[atk.getIndex()]) {
            if(unitMap.containsKey(nearIndex)) {
                resultList.add(unitMap.get(nearIndex));
            }
        }
        return resultList;
    }



}
