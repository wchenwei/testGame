package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.BuffEvent;
import com.hm.war.sg.unit.Unit;
import lombok.Getter;

/**
 * 转移伤害为持续伤害buff
 */
@Getter
public class TransHurtBuff extends BaseBuffer {
    private int hurtVal;//伤害百分比

    public TransHurtBuff(long endFrame, double val, Object confVal) {
        super(UnitBufferType.TransHurtBuff, endFrame, BuffKind.Buff);
        setValue(val);
        this.hurtVal = (int) confVal;
    }

    public void tranHurt(Frame frame, Unit unit, Unit buffUnit, long hurt, int funcId) {
        long count = (long) getValue();
        long frameHurt = Math.max(1, hurt / count);

        int interval = 10;
//        FunctionSetting functionSetting =  SettingManager.getInstance().getTankSkillConfig().getFunctionSetting(funcId);
//        SkillFunction skillFunction = new SkillFunction(1,functionSetting);
//        BuffBear buffBear = new BuffBear(atkId, UnitBufferType.ShedBloodBuff, count*interval, frame.getId(), skillFunction);
//        unit.addBear(frame, buffBear);
        ShedBloodBuff shedBloodBuff = new ShedBloodBuff(buffUnit, frameHurt, frame.getId(), interval, count, funcId);
        shedBloodBuff.setBuffUnitId(buffUnit.getId());
        unit.addBuffer(shedBloodBuff);
        frame.addEvent(new BuffEvent(unit.getId(), UnitBufferType.ShedBloodBuff, count * interval, funcId));
    }
}
