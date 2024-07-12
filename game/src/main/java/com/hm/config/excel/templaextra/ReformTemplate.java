package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.ReformBaseTemplate;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@FileConfig("reform_base")
public class ReformTemplate extends ReformBaseTemplate{
	private List<Items> itemCost = Lists.newArrayList(); //消耗的材料
	private Map<TankAttrType, Double> reformAttrMap = Maps.newHashMap(); //改造基础加成

	private List<Items> totalCost = Lists.newArrayList();
	
	public void init(){
		 this.itemCost = ItemUtils.str2ItemList(getReform_resource(), ",", ":");
		 this.reformAttrMap = TankAttrUtils.str2TankAttrMap(getReform_attr(), ",", ":");
	}

	public void calTotalCost(Collection<ReformTemplate> templateList){
		List<Items> costItems = Lists.newArrayList();
		templateList.stream().filter(e -> e.getReform_level() < getReform_level()).forEach(e -> costItems.addAll(e.getItemCost()));
		this.totalCost = ItemUtils.mergeItemList(costItems);
	}
	
}
