package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName CarModeStartEvent
 * @Deacription CarModeStartEvent
 * @Author zxj
 * @Date 2022/3/11 10:35
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHCommancerCarModelStar)
public class CarModeStartEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9005";
        event_name = "指挥官座驾皮肤升星";

        this.driverid = Convert.toInt(argv[0]);
        this.Level_Before = Convert.toInt(argv[1]);
        this.Level_After = Convert.toInt(argv[2]);
        List<Items> item = (List<Items>) argv[3];
        this.itemid = ItemCountUtil.getIds(item);
        this.count = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
    }

    private int driverid;
    private int Level_Before;
    private int Level_After;
    private String itemid;
    private String count;
    private String remain;
}
