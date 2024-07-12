package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName EquMilitaryLvupEvemt
 * @Deacription TODO
 * @Author zxj
 * @Date 2022/3/11 16:03
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHMasteryLvUp)
public class EquMilitaryLvupEvemt extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9018";
        event_name = "指挥官研修升级";

        //id, index, lv, player.playerMastery().getLv(id, index), cost
        this.partid = Convert.toInt(argv[0]);
        this.index = Convert.toInt(argv[1]);
        this.Level_Before = Convert.toInt(argv[2]);
        this.Level_After = Convert.toInt(argv[3]);

        List<Items> item = (List<Items>) argv[4];
        this.itemtype = ItemCountUtil.getTypes(item);
        this.type = ItemCountUtil.getIds(item);
        this.cost = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
    }
    private int partid;
    private int index;
    private int Level_Before;
    private int Level_After;
    private String itemtype;
    private String type;
    private String cost;
    private String remain;
}
