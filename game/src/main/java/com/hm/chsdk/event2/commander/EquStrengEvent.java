package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName EquStrengEvent
 * @Deacription 装备强化
 * @Author zxj
 * @Date 2022/3/11 14:22
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquStreng)
public class EquStrengEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9010";
        event_name = "指挥官装备强化";

        this.equid = Convert.toInt(argv[0]);
        this.Lv_Before = Convert.toInt(argv[1]);
        this.Lv_After = Convert.toInt(argv[2]);
        List<Items> item = (List<Items>) argv[3];
        this.count = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
        this.type = ItemCountUtil.getIds(item);
    }

    private String type;
    private String count;
    private String remain;
    private int equid;
    private int Lv_Before;
    private int Lv_After;
}
