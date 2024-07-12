package com.hm.config.excel.templaextra;


import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankCrewTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("tank_crew")
public class TankPassengerTemplate extends TankCrewTemplate{
	private Items composeCost;
	private List<Items> retireReward = Lists.newArrayList();
	
	public void init(){
		this.composeCost = ItemUtils.str2Item(this.getCrew_piece(), ":");
		this.retireReward = ItemUtils.str2ItemList(this.getRecycle(), ",", ":");
	}
	
	public Items getComposeCost(){
		return composeCost.clone();
	}

	public List<Items> getRetireReward() {
		return retireReward;
	}
	
}
