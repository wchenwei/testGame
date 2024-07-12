package com.hm.config.excel;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.KfSeasonRankTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

public class KFSeasonServerReward {
	private List<KfSeasonRankTemplate> templateList = Lists.newArrayList();
	
	public void addTemplate(KfSeasonRankTemplate template) {
		ItemUtils.str2DefaultItemList(template.getReward());
		this.templateList.add(template);
	}
	
	public List<Items> getItemList(int rank) {
		for (KfSeasonRankTemplate template : templateList) {
			if(rank >= template.getRank_down() && rank <= template.getRank_up()) {
				return ItemUtils.str2DefaultItemList(template.getReward());
			}
		}
		return Lists.newArrayList();
	}
}
