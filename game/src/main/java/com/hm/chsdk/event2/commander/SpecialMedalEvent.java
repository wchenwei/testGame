package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/3/11 17:47
 */
//@EventMsg(obserEnum = ObservableEnum.SpecialMedalStarOrLvUp)
public class SpecialMedalEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        int type = (int) argv[0]; // 0 合成， 1 升级， 2 升星
        List<Items> cost = (List<Items>) argv[1];
        this.costItemsId = ItemCountUtil.getIds(cost);
        this.residueItems = ItemCountUtil.getRemain(player, cost);
        this.costItemsCount = ItemCountUtil.getCounts(cost);
        this.medalId = (int) argv[2];
        switch (type) {
            case 0:
                this.event_id = "9026";
                this.event_name = "指挥官特殊勋章获取";
                break;
            case 1:
                this.lv = (int) argv[3];
                this.afterLv = (int) argv[4];
                this.event_id = "9027";
                this.event_name = "指挥官特殊勋章升级";
                break;
            case 2:
                this.lv = (int) argv[3];
                this.afterLv = (int) argv[4];
                this.event_id = "9028";
                this.event_name = "指挥官特殊勋章升星";
                break;
        }
    }

    private int medalId;
    private int lv;
    private int afterLv;
    private String costItemsId;
    private String costItemsCount;
    private String residueItems;
}
