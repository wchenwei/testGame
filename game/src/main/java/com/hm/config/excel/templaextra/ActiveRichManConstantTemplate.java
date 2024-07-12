package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.temlate.ActiveRichmanConfigTemplate;
import com.hm.config.excel.temlate.DropTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.RandomRatio;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_richman_config")
public class ActiveRichManConstantTemplate extends ActiveRichmanConfigTemplate{
	private List<Items> costItems = Lists.newArrayList();
	private List<Items> costGolds = Lists.newArrayList();
	private List<Items> loginReward = Lists.newArrayList();
	//免费祭祀权重
	private RandomRatio freeVowRatio;
	//付费祭祀权重
	private RandomRatio vowRatio;
	//付费祭祀价格
	private List<Items> vowCost =Lists.newArrayList();
	public void init(){
		this.costItems = ItemUtils.str2ItemList(this.getCost_item(), ",", ":");
		this.costGolds = ItemUtils.str2ItemList(this.getCost_gold(), ",", ":");
		this.freeVowRatio = new RandomRatio(this.getVow_free_rate());
		this.vowRatio = new RandomRatio(this.getVow_gold_rate());
		this.vowCost = ItemUtils.str2ItemList(this.getVow_gold_cost(), ",", ":");
		this.loginReward = ItemUtils.str2ItemList(this.getLogin_reward(), ",", ":");
	}
	public List<Items> getCostItems(){
		return costItems.stream().map(t->t.clone()).collect(Collectors.toList());
	}
	public List<Items> getCostGolds(int type){
		return type==0?Lists.newArrayList():costGolds.stream().map(t->t.clone()).collect(Collectors.toList());
	}
	
	public List<Items> getBoxReward(){
		DropConfig dropConfig = SpringUtil.getBean(DropConfig.class);
		DropTemplate template = dropConfig.getDropById(this.getBox_drop());
		if(template!=null){
			return template.randomItem();
		}
		return Lists.newArrayList();
	}
	public List<Items> getVowCost() {
		return vowCost;
	} 
	public List<Items> getLoginReward(){
		return loginReward;
	}
	public int getVowRate(int type){
		RandomRatio ratio = type==0?freeVowRatio:vowRatio;
		return ratio.randomRatio();
	}
}
