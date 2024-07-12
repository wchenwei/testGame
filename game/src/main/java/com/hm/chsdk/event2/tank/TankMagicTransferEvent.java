package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克魔改-魔改转移
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.MagicReformTransfer)
public class TankMagicTransferEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, 0);
        List<Integer> tankIds = Convert.toList(Integer.class, argv[0]);
        tankId = tankIds.get(0);
        otherTankId = tankIds.get(1);

        oldValue = JSON.toJSONString(player.playerTank().getTank(tankId).getTankMagicReform());
        newValue = JSON.toJSONString(player.playerTank().getTank(otherTankId).getTankMagicReform());

        List<Items> cost = Convert.toList(Items.class, argv[1]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8016";
        event_name = "坦克魔改-魔改转移";
    }

    /*
    坦克id
当前魔改等级
魔改属性位置id
当前位置魔改属性等级
改变后位置魔改属性等级
消耗道具id
消耗道具数量
剩余道具数量
     */
    private int otherTankId;
    private String oldValue;
    private String newValue;
    private String costIds;
    private String costCnts;
    private String remains;
}
