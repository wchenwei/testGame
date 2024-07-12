package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName MilitaryLvupEvent
 * @Deacription 军阵升级
 * @Author zxj
 * @Date 2022/3/11 17:01
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.MilitaryLvup)
public class MilitaryLvupEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9021";
        event_name = "指挥官军阵助阵升级";

        this.index = Convert.toInt(argv[0]);
        this.Level_Before = Convert.toInt(argv[1]);
        this.Level_After = Convert.toInt(argv[2]);
        List<Items> item = (List<Items>) argv[3];
        this.type = ItemCountUtil.getIds(item);
        this.cost = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
    }
    private int index;
    private int Level_Before;
    private int Level_After;
    private String type;
    private String cost;
    private String remain;

}
