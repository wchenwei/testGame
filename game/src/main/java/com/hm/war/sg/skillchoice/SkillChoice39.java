package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import com.hm.enums.TankAttrType;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 敌方攻击最高的n个单位39#2
 */
public class SkillChoice39 extends BaseSkillChoice {
    private int num;//随机个数
    private boolean isAtk;
    private boolean isTop;
    private TankAttrType attrType;

    public SkillChoice39(boolean isAtk, boolean isTop, TankAttrType attrType) {
        this.isAtk = isAtk;
        this.isTop = isTop;
        this.attrType = attrType;
    }

    @Override
    public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
        List<Unit> unitList = isAtk ? atkGroup.getLifeUnit() : defGroup.getLifeUnit();
        if (isTop) {
            unitList = unitList.stream().sorted(Comparator.comparingDouble(e -> getAttr((Unit) e)).reversed())
                    .collect(Collectors.toList());
        } else {
            unitList = unitList.stream().sorted(Comparator.comparingDouble(e -> getAttr(e))).collect(Collectors.toList());
        }
        return CollUtil.sub(unitList, 0, this.num);
    }

    public double getAttr(Unit unit) {
        return unit.getUnitAttr().getDoubleValue(attrType);
    }

    @Override
    public void loadParm(String parm) {
        this.num = Integer.parseInt(parm);
    }

}
