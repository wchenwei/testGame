package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ResourceFindbackTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("resource_findback")
public class ResBackTemplate extends ResourceFindbackTemplate{
	private List<Items> normalCost = Lists.newArrayList();
	private List<Items> glodCost = Lists.newArrayList();
	
	public void init(){
		this.normalCost = ItemUtils.str2ItemList(this.getCost_normal(), ",", ":");
		this.glodCost = ItemUtils.str2ItemList(this.getCost_gold(), ",", ":");
	}

	public List<Items> getNormalCost() {
		return normalCost.stream().map(t-> t.clone()).collect(Collectors.toList());
	}

	public List<Items> getGlodCost() {
		return glodCost.stream().map(t-> t.clone()).collect(Collectors.toList());
	}
	
	
}
