package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

/**
 * @ClassName DeiverLvupEvent
 * @Deacription DeiverLvupEvent
 * @Author zxj
 * @Date 2022/3/11 10:02
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHCommancerDriverLvUpEvent)
public class DeiverLvupEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9002";
        event_name = "指挥官座驾升级";

        this.Level_Before = Convert.toInt(argv[0]);
        this.Level_After = Convert.toInt(argv[1]);
        Items item = (Items) argv[2];
        this.itemid = item.getId();
        this.count = item.getCount();
        this.remain = ItemCountUtil.getRemain(player, item);
    }

    private int Level_Before;
    private int Level_After;
    private int itemid;
    private long count;
    private long remain;
}
