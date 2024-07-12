package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName EquWeaponLvEvent
 * @Deacription 超级武器升级
 * @Author zxj
 * @Date 2022/3/11 15:35
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquWeaponLv)
public class EquWeaponLvEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9017";
        event_name = "指挥官超级武器";

        this.Level_Before = Convert.toInt(argv[0]);
        this.Level_After = Convert.toInt(argv[1]);

        List<Items> item = (List<Items>) argv[2];
        this.cost = ItemCountUtil.getCounts(item);
        this.remain = ItemCountUtil.getRemain(player, item);
        this.type = ItemCountUtil.getIds(item);
    }

    private int Level_Before;
    private int Level_After;
    private String type;
    private String cost;
    private String remain;
}
