package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.RechargeGiftTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

@FileConfig("recharge_gift")
public class RechargeGiftTempImpl extends RechargeGiftTemplate{
	public List<Items> getItemList(Player player) {
		List<Items> itemList = Lists.newArrayList();
		if(getDiamonds() <= 0) {
			return itemList;
		}
		Items item = new Items(PlayerAssetEnum.Gold.getTypeId(),getDiamonds(),ItemType.CURRENCY.getType());
		if(item.getCount() > 0) {
			itemList.add(item);
			long rechargeCount = player.getPlayerRecharge().getRechargeStatistics(getId());
			boolean isDouble = rechargeCount == 0 && getFirst_double() == 1;
			if(isDouble) {
				//计算双倍
				item.setCount(item.getCount()*2);
			}else{
				//计算额外
				Items elseitem = new Items(PlayerAssetEnum.Gold.getTypeId(),getElse_diamonds(),ItemType.CURRENCY.getType());
				if(elseitem.getCount() > 0) {
					itemList.add(elseitem);
				}
			}
		}
		return itemList;
	}
}
