package com.hm.model.ticket;

import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2020/10/23 17:29
 */
public abstract class Ticket {

    /**
     * @description
     *          获取用户实际消耗的道具集合
     * @param player
     * @param items 需要消耗的物品
     * @param num  购买物品数量
     * @param itemTemplate 优惠券信息
     * @return java.util.List<com.hm.model.item.Items>
     * @author wyp
     * @date 2020/10/24 9:12
     */
    public abstract List<Items> getRealCost(Player player, List<Items> items, int num, ItemTemplate itemTemplate);
}
