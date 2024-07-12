package com.hm.action.flop.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.enums.LogType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.weight.Drop;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Biz
public class FlopBiz {
	@Resource
	private ItemBiz itemBiz;

	public List<Items> createFlop(Player player,List<Drop> drops){
		if(drops.isEmpty()){
			return Lists.newArrayList();
		}
//		if(!player.PlayerFlop().getCards().isEmpty()){
//			List<Items> lastRewards = player.PlayerFlop().getReward(1);
//			player.PlayerFlop().clearFlop();
//			itemBiz.addItem(player, lastRewards, LogType.Flop);
//		}
		List<Items> rewards =  getDropItemList(drops);
//		player.PlayerFlop().createCards(rewards);
		return rewards;
	}
	
	
	//掉落
	public List<Items> getDropItemList(List<Drop> drops) {
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:drops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		Collections.shuffle(dropItems);
		itemBiz.createItemList(dropItems);
		return dropItems;
	}


}
