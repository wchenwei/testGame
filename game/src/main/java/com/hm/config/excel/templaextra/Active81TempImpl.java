package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active81SearchTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;

@FileConfig("active_81_search")
public class Active81TempImpl extends Active81SearchTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private Map<Integer, Integer> weightMap = Maps.newHashMap();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getDrop(), ",", ":");
		if(!StringUtil.isNullOrEmpty(this.getDrop_last())) {
			weightMap = StringUtil.strToMap(this.getDrop_last(), ",", ":");
		}
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public Map<Integer, Integer> getWeightMap() {
		return weightMap;
	}
}
