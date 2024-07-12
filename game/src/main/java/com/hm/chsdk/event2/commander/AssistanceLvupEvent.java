package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName AssistanceLvupEvent
 * @Deacription 军阵助阵升级
 * @Author zxj
 * @Date 2022/3/11 16:54
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.MilitaryAssistanceLvup)
public class AssistanceLvupEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9020";
        event_name = "指挥官军阵助阵升级";
        this.index = Convert.toInt(argv[0]);
        this.Level_Before = Convert.toInt(argv[1]);
        this.Level_After = Convert.toInt(argv[2]);
    }
    private int index;
    private int Level_Before;
    private int Level_After;
}
