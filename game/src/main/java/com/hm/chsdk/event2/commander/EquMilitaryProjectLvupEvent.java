package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName EquMilitaryProjectLvupEvent
 * @Deacription EquMilitaryProjectLvupEvent
 * @Author zxj
 * @Date 2022/3/11 15:24
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquMilitaryProjectLvUp)
public class EquMilitaryProjectLvupEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9016";
        event_name = "指挥官军功";

        List<Items> item = (List<Items>) argv[0];
        this.paperid = ItemCountUtil.getIds(item);
        this.cost = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
        this.Level_Before = Convert.toInt(argv[1]);
        this.Level_After = Convert.toInt(argv[2]);
    }
    private int Level_Before;
    private int Level_After;
    private String paperid;
    private String cost;
    private String remain;
}
