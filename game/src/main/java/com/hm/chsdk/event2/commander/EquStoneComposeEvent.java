package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName EquStoneComposeEvent
 * @Deacription 合成宝石
 * @Author zxj
 * @Date 2022/3/11 14:55
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquStoneCompose)
public class EquStoneComposeEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9013";
        event_name = "指挥官装备宝石合成";
        //stoneId, count, player.playerStone().getItemCount(stoneId), nextId, num
        this.stoneid = Convert.toInt(argv[0]);
        this.count = Convert.toInt(argv[1]);
        this.remain = Convert.toInt(argv[2]);
        this.getstone = Convert.toInt(argv[3]);
        this.getcount = Convert.toInt(argv[4]);
    }


    private int stoneid;
    private long count;
    private long remain;
    private int getstone;
    private long getcount;

}
