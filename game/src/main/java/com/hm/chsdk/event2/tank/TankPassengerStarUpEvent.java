package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克乘员培养-晋升
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.Passenger_StarUp)
public class TankPassengerStarUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        id = Convert.toInt(argv[1]);
        oldStar = Convert.toInt(argv[2]);
        newStar = Convert.toInt(argv[3]);

        List<Items> cost = Convert.toList(Items.class, argv[4]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8023";
        event_name = "坦克乘员培养-晋升";
    }

    private int id;
    private int oldStar;
    private int newStar;

    private String costIds;
    private String costCnts;
    private String remains;
}
