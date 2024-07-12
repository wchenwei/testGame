package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.DriverLevelTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@Getter
@FileConfig("driver_level")
public class DriverLvTemplate extends DriverLevelTemplate {
	private List<Items> itemCost = Lists.newArrayList(); //消耗的材料
	private Integer needEvolveLv;

	private long totalCostSoul;
	private List<Items> totalCostItems = Lists.newArrayList();

	@ConfigInit
	public void init(){
		this.itemCost = ItemUtils.str2ItemList(getCost_item(), ",", ":");
	}

	public void init(List<DriverLvTemplate> templateList){
		calTotalCost(templateList);
		this.needEvolveLv = templateList.stream()
				.filter(e -> e.getLevel() >= getLevel())
				.filter(e-> e.getRare_id() == getRare_id())
				.mapToInt(DriverLevelTemplate::getEvolve_level)
						.filter(e -> e > 0)
						.min()
						.orElse(1) - 1;
	}

	public void calTotalCost(List<DriverLvTemplate> templateList){
		templateList.stream().filter(e -> e.getRare_id() == getRare_id()).filter(e -> e.getLevel() < getLevel()).forEach(e -> {
			totalCostItems.addAll(e.getItemCost());
			totalCostSoul += e.getCost_soul();
		});
		this.totalCostItems = ItemUtils.mergeItemList(totalCostItems);
	}

	public int getBeforeLv(){
		return getLevel() -1;
	}

	
}
