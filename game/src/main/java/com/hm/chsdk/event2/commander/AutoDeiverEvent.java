package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName AutoDeiverEvent
 * @Deacription AutoDeiverEvent
 * @Author zxj
 * @Date 2022/3/11 10:02
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHCommancerAutoDriveLvUp)
public class AutoDeiverEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9006";
        event_name = "指挥官座驾智能驾驶升级";

        this.Level_Before = Convert.toInt(argv[0]);
        this.Level_After = Convert.toInt(argv[1]);
        List<Items> item = (List<Items>) argv[2];
        this.itemid = ItemCountUtil.getIds(item);
        this.count = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
    }

    private int Level_Before;
    private int Level_After;
    private String itemid;
    private String count;
    private String remain;
}
