package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName MilitaryLineupEvent
 * @Deacription 军阵
 * @Author zxj
 * @Date 2022/3/11 16:32
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHMilitaryLineup)
public class MilitaryLineupEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9019";
        event_name = "指挥官军阵上阵战车";
        this.before = Convert.toStr(argv[0]);
        this.after = Convert.toStr(argv[1]);
    }
    private String before;
    private String after;
}
