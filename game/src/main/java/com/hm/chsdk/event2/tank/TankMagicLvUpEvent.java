package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.ItemUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克魔改
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.MagicReform)
public class TankMagicLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        lv = Convert.toInt(argv[1]);
        skillId = Convert.toInt(argv[2]);
        oldSkillLv = Convert.toInt(argv[3]);
        newSkillLv = Convert.toInt(argv[4]);
        List<Items> cost = Convert.toList(Items.class, argv[5]);
        costIds = ItemUtil.joinItemId(cost);
        costCnts = ItemUtil.joinItemCnt(cost);
        remains = ItemUtil.joinItemRemain(player, cost);

        event_id = "8015";
        event_name = "坦克魔改";
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
    private int lv;
    private int skillId;
    private int oldSkillLv;
    private int newSkillLv;
    private String costIds;
    private String costCnts;
    private String remains;
}
