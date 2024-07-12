package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.aircraft.GroupAircraft;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 增加飞机机油
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2021/10/26 10:00
 */
public class AddAircraftOilEffect extends BaseSkillEffect {

    public AddAircraftOilEffect() {
        super(SkillEffectType.AddAircraftOilEffect);
    }

    @Override
    public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
        GroupAircraft groupAircraft = unit.getMyGroup().getGroupAircraft();
        if (groupAircraft != null) {
            groupAircraft.addCurMp(getValue(unit, unit, skill, skillFunction));
        }
    }


    public int getValue(Unit atk, Unit def, Skill skill, SkillFunction skillFunction) {
        return (int) skillFunction.getFunctionValue(atk, def, skill);
    }

}
