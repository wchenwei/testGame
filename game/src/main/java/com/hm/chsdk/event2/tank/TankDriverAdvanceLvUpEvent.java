package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克车长-车长军职
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankDriverAdvanceLvUp)
public class TankDriverAdvanceLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        id = Convert.toInt(argv[1]);
        advance = Convert.toStr(argv[2]);
        List<Items> cost = Convert.toList(Items.class, argv[3]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8011";
        event_name = "坦克车长-车长军职";
    }

    /*
    坦克id
变更后军职id
升职所选服役部队和属性
消耗道具id
消耗道具数量
剩余道具数量
    * */
    private int id;
    private String  advance;
    private String costIds;
    private String costCnts;
    private String remains;
}
