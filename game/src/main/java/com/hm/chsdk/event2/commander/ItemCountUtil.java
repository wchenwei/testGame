package com.hm.chsdk.event2.commander;

import com.google.common.base.Joiner;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ItemCountUtil
 * @Deacription TODO
 * @Author zxj
 * @Date 2022/3/11 9:33
 * @Version 1.0
 **/
public class ItemCountUtil {
    public static String getTypes(List<Items> costItems) {
        return Joiner.on(",").join(costItems.stream().map(Items::getItemType).collect(Collectors.toList()));
    }
    public static String getIds(List<Items> costItems) {
        return Joiner.on(",").join(costItems.stream().map(Items::getId).collect(Collectors.toList()));
    }
    public static String getCounts(List<Items> costItems) {
        return Joiner.on(",").join(costItems.stream().map(Items::getCount).collect(Collectors.toList()));
    }

    public static String getRemain(Player player, List<Items> costItems) {
        ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
        return Joiner.on(",").join(costItems.stream().map(e -> itemBiz.getItemTotal(player, e.getItemType(), e.getId())).collect(Collectors.toList()));
    }

    public static long getRemain(Player player, Items costItems) {
        ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
        return itemBiz.getItemTotal(player, costItems.getItemType(), costItems.getId());
    }
}
