package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克进化
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.TankEvolveStarUp)
public class TankSeniorStarUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        lv = Convert.toInt(argv[1]);
        List<Items> cost = Convert.toList(Items.class, argv[2]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8013";
        event_name = "坦克进化";
    }

    /*
    坦克id
进化后坦克星级
消耗道具id
消耗道具数量
剩余道具数量
    * */
    private int lv;
    private String costIds;
    private String costCnts;
    private String remains;
}
