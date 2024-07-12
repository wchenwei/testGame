package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官航母舰岛升级
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.AircraftIslandLvup)
public class AircraftIslandLvUpEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9036";
        this.event_name = "指挥官航母舰岛升级";
        this.id = (int) argv[0];
        this.landId = (int) argv[1];
        this.nextIsLand = (int) argv[2];
        List<Items> cost = (List<Items>) argv[3];
        this.costItemsId = ItemCountUtil.getIds(cost);
        this.residueItems = ItemCountUtil.getRemain(player, cost);
        this.costItemsCount = ItemCountUtil.getCounts(cost);
    }

    private int id;
    private int landId;
    private int nextIsLand;
    private String costItemsId;
    private String costItemsCount;
    private String residueItems;
}
