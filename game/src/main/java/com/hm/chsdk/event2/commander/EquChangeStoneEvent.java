package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName EquChangeStoneEvent
 * @Deacription 更换石头
 * @Author zxj
 * @Date 2022/3/11 14:41
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquChangeStone)
public class EquChangeStoneEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9012";
        event_name = "指挥官装备宝石镶嵌";

        //id, index, equ.getEquId(), stoneId
        this.equid = Convert.toInt(argv[0]);
        this.index = Convert.toInt(argv[1]);
        this.Id_Before = Convert.toInt(argv[2]);
        this.Id_After = Convert.toInt(argv[3]);
    }
    private int equid;
    private int index;
    private int Id_Before;
    private int Id_After;
}
