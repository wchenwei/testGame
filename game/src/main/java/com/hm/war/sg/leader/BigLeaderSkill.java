package com.hm.war.sg.leader;

import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarComm;
import com.hm.war.sg.buff.SkillPreBuff;
import com.hm.war.sg.event.SkillPreEvent;
import com.hm.war.sg.unit.Unit;
import lombok.Getter;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 军衔大招技能
 * @date 2024/4/9 15:42
 */
@Getter
public class BigLeaderSkill extends BaseLeaderSkill{
    private SkillPreBuff skillPreBuff;//技能前摇结束时间

    public BigLeaderSkill(int lv, int cd) {
        super(WarComm.MLBigSkillId,lv,cd);
    }


    @Override
    public boolean checkReleaseSkill(Frame frame, UnitGroup atkGroup, UnitGroup defGroup) {
        if(this.skillPreBuff != null) {
            if(this.skillPreBuff.getEndFrame() <= frame.getId()) {//释放技能
                this.skillPreBuff = null;
                super.checkReleaseSkill(frame,atkGroup,defGroup);
            }
            return true;
        }

        if(frame.getId() < this.nextFrame) {
            return false;
        }
        Unit unit = buildLeaderUnit(atkGroup,defGroup);
        this.skillPreBuff = new SkillPreBuff(skill.getPreFrame()+frame.getId(), skillId);
        frame.addEvent(new SkillPreEvent(unit, skillId,skill.getPreFrame()));

        return true;
    }

    @Override
    public List<Unit> getSkillUnitList(UnitGroup defGroup) {
        return defGroup.getLifeUnit();
    }

}
