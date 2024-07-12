package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active815FormulaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;
/**
 * ClassName: Active815FormulaImpl. <br/>  
 * Function: 中秋节活动，月饼兑换配置表. <br/>  
 * date: 2019年8月5日 上午11:09:22 <br/>  
 * @author zxj  
 * @version
 */
@FileConfig("active_815_formula")
public class Active815FormulaImpl extends Active815FormulaTemplate{
	//月饼奖励
	private List<Items> rewards = Lists.newArrayList();
	//合成月饼消耗
	private List<Items> cost = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getProduct(), ",", ":");
		this.cost = ItemUtils.str2ItemList(this.getFormula(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	public List<Items> getCost() {
		return cost.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	
	public boolean checkLv(int lv) {
		return lv>=this.getServer_level_low() && lv<= this.getServer_level_high();
	}
}



