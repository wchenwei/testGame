package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GuildFactoryBuildTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("guild_factory_build")
public class GuildFactoryBuildExtraTemplate extends GuildFactoryBuildTemplate{
	private List<Items> cost =Lists.newArrayList();
	private Drop drop ;
	
	public void init(){
		this.cost = ItemUtils.str2ItemList(this.getItem(), ",",":");
		this.drop = new Drop(this.getReward());
	}
	
	public Items randomReward(){
		return this.drop.randomItem();
	}
	public List<Items> getCost(){
		return Lists.newArrayList(cost);
	}
}
