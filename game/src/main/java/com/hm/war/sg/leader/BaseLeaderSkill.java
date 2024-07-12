package com.hm.war.sg.leader;

import com.hm.config.excel.TankSkillConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.unit.Unit;
import lombok.Getter;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Getter
public abstract class BaseLeaderSkill {
    public long cd;
    public long nextFrame;//下次释放技能时间
    public int skillId;
    public int lv;

    @Transient
    public transient Skill skill;

    public BaseLeaderSkill(int skillId, int lv, int cd) {
        this.skillId = skillId;
        this.lv = lv;
        this.cd = cd;
        this.nextFrame = this.cd;
        //加载技能
        TankSkillConfig tankSkillConfig = SpringUtil.getBean(TankSkillConfig.class);
        this.skill = new Skill(this.lv, tankSkillConfig.getSkillSetting(skillId));
    }

    public boolean checkCD(Frame frame) {
        return this.nextFrame <= frame.getId();
    }

    public void changeNextFrame(long nextFrame) {
        this.nextFrame = Math.max(this.nextFrame,nextFrame);
    }


    public boolean checkReleaseSkill(Frame frame, UnitGroup atkGroup, UnitGroup defGroup) {
        this.nextFrame = frame.getId() + this.cd;

        Unit unit = buildLeaderUnit(atkGroup,defGroup);
        skill.doActiveSkillForTargetList(frame, unit, atkGroup, defGroup,getSkillUnitList(defGroup));
        return false;
    }


    public Unit buildLeaderUnit(UnitGroup atkGroup, UnitGroup defGroup) {
        Unit unit = new Unit(atkGroup.getId());
        unit.setIndex(-1);
        unit.setMyGroup(atkGroup);
        unit.setDefGroup(defGroup);
        return unit;
    }

    public abstract List<Unit> getSkillUnitList(UnitGroup defGroup);

}
