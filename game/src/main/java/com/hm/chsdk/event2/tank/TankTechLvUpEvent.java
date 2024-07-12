package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克突破
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.TankTechUpdate)
public class TankTechLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        chipId = Convert.toInt(argv[1]);
        value = Convert.toStr(argv[2]);
        List<Items> cost = Convert.toList(Items.class, argv[3]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8012";
        event_name = "坦克突破";
    }

    /*
    坦克id
科技类型
突破属性id
消耗道具id
消耗道具数量
剩余道具数量
    * */
    private int chipId;
    private String  value;
    private String costIds;
    private String costCnts;
    private String remains;
}
