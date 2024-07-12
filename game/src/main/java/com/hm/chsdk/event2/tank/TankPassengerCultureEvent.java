package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克乘员培养-特长
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.Passenger_Culture)
public class TankPassengerCultureEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        oldStr = Convert.toStr(argv[1]);
        newStr = Convert.toStr(argv[2]);

        List<Items> cost = Convert.toList(Items.class, argv[3]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8022";
        event_name = "坦克乘员培养-特长";
    }

    private String oldStr;
    private String newStr;

    private String costIds;
    private String costCnts;
    private String remains;
}
