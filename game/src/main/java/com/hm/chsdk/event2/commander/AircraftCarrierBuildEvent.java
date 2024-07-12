package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官航母动力舱修造
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.AircraftCarrierBuild)
public class AircraftCarrierBuildEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9032";
        this.event_name = "指挥官航母动力舱修造";
        this.engineLv = (int) argv[0];
        this.afterEngineLv = (int) argv[1];
        List<Items> cost = (List<Items>) argv[2];
        this.costItemsId = ItemCountUtil.getIds(cost);
        this.residueItems = ItemCountUtil.getRemain(player, cost);
        this.costItemsCount = ItemCountUtil.getCounts(cost);
    }

    private int engineLv;
    private int afterEngineLv;
    private String costItemsId;
    private String costItemsCount;
    private String residueItems;
}
