package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName RefineEvent
 * @Deacription 宝石精炼
 * @Author zxj
 * @Date 2022/3/11 18:24
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHRefine)
public class RefineEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9011";
        event_name = "宝石精炼";
        partid = Convert.toInt(argv[0]);
        Lv_After = Convert.toInt(argv[1]);
        Lv_After = Convert.toInt(argv[2]);

        List<Items> item = (List<Items>) argv[3];

        this.count = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
    }
    private int partid;
    private int Lv_Before;
    private int Lv_After;
    private String count;
    private String remain;

}
