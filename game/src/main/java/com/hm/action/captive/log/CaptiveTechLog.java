package com.hm.action.captive.log;

import com.hm.enums.CaptiveTankLogType;
import com.hm.model.item.Items;

import java.util.List;

/**
 * @description: 战俘研究记录
 * @author: chenwei
 * @create: 2020-07-11 15:39
 **/
public class CaptiveTechLog extends AbstractCaptiveLog {
    private String name;
    private int tankId;
    private List<Items> rewards;

    public CaptiveTechLog(int tankId, String name, List<Items> rewards) {
        super(CaptiveTankLogType.CaptiveTech);
        this.tankId = tankId;
        this.name = name;
        this.rewards = rewards;
    }
}
