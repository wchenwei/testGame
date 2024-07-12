package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克车长-车长等级
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankDriverLvUp)
public class TankDriverLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        oldLv = Convert.toInt(argv[1]);
        newLv = Convert.toInt(argv[2]);
        List<Items> cost = Convert.toList(Items.class, argv[3]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8009";
        event_name = "坦克车长-车长等级";
    }

    /*
    坦克车长id
当前车长等级
改变后车长等级
消耗道具id
消耗道具数量
剩余道具数量
    * */
    private int oldLv;
    private int newLv;
    private String costIds;
    private String costCnts;
    private String remains;
}
