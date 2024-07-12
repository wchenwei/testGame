package com.hm.war.sg.buff;

import com.hm.war.sg.unit.Unit;

/**
 * 持续掉血buff
 */
public class ShedBloodBuff extends ContinueBuff {

    public ShedBloodBuff(Unit buffUnit, long reduceHp, long effectFrame, int interval, long count, int funcId) {
        super(buffUnit, -reduceHp, 0, effectFrame, interval, count, funcId, BuffKind.None);
        setType(UnitBufferType.ShedBloodBuff);
    }

    @Override
    public boolean isForoverBuff() {
        return false;
    }
}
