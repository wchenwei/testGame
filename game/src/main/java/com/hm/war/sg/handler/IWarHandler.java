package com.hm.war.sg.handler;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 战斗第三方处理器
 * @date 2024/4/30 9:09
 */
public interface IWarHandler {
    void doUnitDeath(Frame frame, Unit unit);
}
