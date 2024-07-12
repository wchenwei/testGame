package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克中控-元件升级
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.ElementLvUp)
public class TankCtrlLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        newEleId = Convert.toInt(argv[1]);
        oldEleId = Convert.toInt(argv[2]);
        List<Items> cost = Convert.toList(Items.class, argv[3]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8018";
        event_name = "坦克中控-元件升级";
    }

    /*
    坦克id
当前装配原件id
改变装配原件id
消耗道具id
消耗道具数量
剩余道具数量

     */
    private int oldEleId;
    private int newEleId;
    private String costIds;
    private String costCnts;
    private String remains;
}
