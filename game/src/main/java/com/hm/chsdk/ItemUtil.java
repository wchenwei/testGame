package com.hm.chsdk;

import com.google.common.base.Joiner;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;

import java.util.Collection;
import java.util.stream.Collectors;

public class ItemUtil {

    public static String joinItemId(Collection<Items> items) {
        return Joiner.on(",").join(items.stream().map(Items::getId).collect(Collectors.toList()));
    }

    public static String joinItemCnt(Collection<Items> items) {
        return Joiner.on(",").join(items.stream().map(Items::getCount).collect(Collectors.toList()));
    }

    public static String joinItemRemain(BasePlayer player, Collection<Items> items) {
        ItemBiz bean = SpringUtil.getBean(ItemBiz.class);
        return Joiner.on(",").join(items.stream().map(e -> bean.getItemTotal(player, e.getItemType(), e.getId())).collect(Collectors.toList()));
    }
}
