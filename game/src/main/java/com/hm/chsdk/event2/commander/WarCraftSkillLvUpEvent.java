package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官兵法技能升级
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.WarCraftSkillLvup)
public class WarCraftSkillLvUpEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9030";
        this.event_name = "指挥官兵法技能升级";
        this.skillId = (int) argv[2];
        this.lv = (int) argv[3];
        this.afterLv = (int) argv[4];
        List<Items> cost = (List<Items>) argv[5];
        this.costItemsId = ItemCountUtil.getIds(cost);
        this.residueItems = ItemCountUtil.getRemain(player, cost);
        this.costItemsCount = ItemCountUtil.getCounts(cost);
    }

    private int skillId;
    private int lv;
    private int afterLv;
    private String costItemsId;
    private String costItemsCount;
    private String residueItems;
}
