package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName SetCarIconEvent
 * @Deacription SetCarIconEvent
 * @Author zxj
 * @Date 2022/3/11 11:48
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHCommancerSetCarIcon)
public class SetCarIconEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9007";
        event_name = "指挥官座驾皮肤切换";

        Id_Before = Convert.toInt(argv[0]);
        Id_After = Convert.toInt(argv[1]);
    }

    private int Id_Before;
    private int Id_After;
}
