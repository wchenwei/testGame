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
 *          抵扣券信息，单个物品达到阈值即可使用，
 *          一个物品最多使用一张，默认背包里面有多少张券，最多用跟购买商品数量一致
 *          例如：抵扣券额度 500砖，商品价值200 砖。购买 3 个物品，所需 400 砖 + 1张抵扣券
 * @date 2020/10/23 17:36
 */
@Data
@NoArgsConstructor
public class DeductionTicket extends Ticket{
    /**
     * 阈值
     */
    private int needNum;
    /**
     * 该券可以抵扣的额度
     */
    private int deduction;

    public DeductionTicket(ItemTemplate itemTemplate){
        int[] ints = StringUtil.strToIntArray(itemTemplate.getEffect(), ",");
        this.needNum = ints[0];
        this.deduction = ints[1];
    }

    @Override
    public List<Items> getRealCost(Player player, List<Items> cost, int num, ItemTemplate itemTemplate) {
        // 用户背包拥有的券数量
        long itemCount = player.playerBag().getItemCount(itemTemplate.getId());
        if(itemCount < 1){
            return ItemUtils.calItemRateReward(cost, num);
        }
        // 实际使用券数量，取背包中与购买数量的最小值
        long usedCount = Math.min(itemCount, num);
        List<Items> list = Lists.newArrayList();
        cost.stream().forEach(items -> {
            Items clone = items.clone();
            long realPrice = clone.getCount() * num;
            // 单个物品价格的到达额度抵扣
            if(items.getCount() >= this.needNum){
                // 抵扣后得价格
                realPrice = realPrice -  usedCount * (Math.min(this.deduction, items.getCount())) ;
            }
            clone.setCount(realPrice);
            list.add(clone);
        });
        //抵扣券添加到使用道具中
        list.add(new Items(itemTemplate.getId(), usedCount, ItemType.ITEM.getType()));
        return list;
    }
}
