package com.hm.war.sg.leader;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarComm;
import com.hm.war.sg.unit.Unit;
import com.hm.util.RandomUtils;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 军衔普攻技能
 * @date 2024/4/9 15:42
 */
public class NormalLeaderSkill extends BaseLeaderSkill{
    @Transient
    private transient Unit defUnit;

    public NormalLeaderSkill(int lv, int cd) {
        super(WarComm.MLNormalSkillId,lv,cd);
    }


    @Override
    public List<Unit> getSkillUnitList(UnitGroup defGroup) {
        if(this.defUnit == null || this.defUnit.isDeath()) {
            //找第一排的坦克
            List<Unit> defList = ChoiceUtils.getFirstRowUnit(defGroup.getLifeUnit());
            if(CollUtil.isEmpty(defList)) {
                return Lists.newArrayList();
            }
            this.defUnit = RandomUtils.randomEle(defList);
        }
        return Lists.newArrayList(this.defUnit);
    }

}
