package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克中控-元件转移
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.ControlTransfer)
public class TankCtrlTransferEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, 0);
        List<Integer> tankIds = Convert.toList(Integer.class, argv[0]);
        tankId = tankIds.get(0);
        newTankId = tankIds.get(1);

        List<Items> cost = Convert.toList(Items.class, argv[1]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8019";
        event_name = "坦克中控-元件转移";
    }

    /*
    被转移坦克id
转移坦克id
消耗道具id
消耗道具数量
剩余道具数量
     */
    private int newTankId;
    private String costIds;
    private String costCnts;
    private String remains;
}
