package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName CarModeEvent
 * @Deacription CarModeEvent
 * @Author zxj
 * @Date 2022/3/11 10:35
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHCommancerUnlockCarModel)
public class CarModeEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9004";
        event_name = "指挥官座驾皮肤获得";

        this.driverid = Convert.toInt(argv[0]);
        List<Items> item = (List<Items>) argv[1];
        this.itemid = ItemCountUtil.getIds(item);
        this.count = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
    }

    private int driverid;
    private String itemid;
    private String count;
    private String remain;
}
