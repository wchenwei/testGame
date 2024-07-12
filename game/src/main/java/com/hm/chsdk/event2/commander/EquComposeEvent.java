package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

/**
 * @ClassName EquComposeEvent
 * @Deacription EquComposeEvent
 * @Author zxj
 * @Date 2022/3/11 13:59
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquCompose)
public class EquComposeEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9009";
        event_name = "指挥官装备合成";

        Items item = (Items) argv[0];
        peaceid =item.getId();
        count = item.getCount();
        remain = ItemCountUtil.getRemain(player, item);
        equid = Convert.toInt(argv[1]);
    }

    private int peaceid;
    private long count;
    private long remain;
    private int equid;

}
