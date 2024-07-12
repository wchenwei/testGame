package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-22
 */
@FileConfig("item_tank_skill_upgrade")
public class ItemTankSkillUpgradeTemplateImpl extends ItemTankSkillUpgradeTemplate {
    private Items money;
    private Items piece;
    private List<Items> rebate = Lists.newArrayList();

    public void init() {
        money = ItemUtils.str2Item(this.getUp_cost_money(), ":");
        piece = ItemUtils.str2Item(this.getUp_cost_piece(), ":");
        rebate = ItemUtils.str2ItemList(this.getReturn_cost(), ",", ":");
    }

    /**
     * 升级消耗资源
     *
     * @return
     */
    public Items getMoney() {
        return money;
    }

    /**
     * 升级消耗碎片
     *
     * @return
     */
    public Items getPiece() {
        return piece;
    }

    /**
     * 还原获得
     *
     * @return
     */
    public List<Items> getRebate() {
        return rebate;
    }
}
