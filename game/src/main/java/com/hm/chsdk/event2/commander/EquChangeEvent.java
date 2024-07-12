package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName EquChangeEvent
 * @Deacription 装备改变
 * @Author zxj
 * @Date 2022/3/11 13:52
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquChange)
public class EquChangeEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9008";
        event_name = "指挥官装备信息";

        this.equid = Convert.toInt(argv[0]);
        this.Id_Before = Convert.toInt(argv[1]);
        this.Id_After = Convert.toInt(argv[2]);
    }
    private int equid;
    private int Id_Before;
    private int Id_After;
}
