package com.hm.model.ticket;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wyp
 * @description
 *          折扣券信息，单次购买物品总价值达到阈值即可使用一张，
 *          且每次最多使用一张
 * @date 2020/10/23 16:56
 */
@Data
@NoArgsConstructor
public class DiscountTicket extends Ticket{
    /**
     * 阈值
     */
    private int needNum;
    /**
     * 该券的折扣值  例如：0.5
     */
    private double discount;

    public DiscountTicket(ItemTemplate itemTemplate){
        String effectStr = itemTemplate.getEffect();
        double[] doubles = StringUtil.strToDoubleArray(effectStr, ",");
        this.needNum = (int)doubles[0];
        this.discount = doubles[1];
    }

    @Override
    public List<Items> getRealCost(Player player, List<Items> cost, int num, ItemTemplate itemTemplate) {
        long itemCount = player.playerBag().getItemCount(itemTemplate.getId());
        if(itemCount < 1){
            return ItemUtils.calItemRateReward(cost, num);
        }
        List<Items> list = Lists.newArrayList();
        boolean isUsedTicket =false;
        for(Items items : cost){
            Items clone = items.clone();
            long realPrice = clone.getCount() * num;
            // 总价格的到达额度折扣
            if(realPrice >= this.needNum){
                // 折扣后得价格
                isUsedTicket = true;
                realPrice = (long) (realPrice * this.discount);
            }
            clone.setCount(realPrice);
            list.add(clone);
        }
        if(isUsedTicket){
            //折扣券添加到使用道具中
            list.add(new Items(itemTemplate.getId(),1, ItemType.ITEM.getType()));
        }
        return list;
    }
}
