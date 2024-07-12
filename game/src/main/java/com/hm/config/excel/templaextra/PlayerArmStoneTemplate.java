package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerArmGemTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@FileConfig("player_arm_gem")
public class PlayerArmStoneTemplate extends PlayerArmGemTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
	//合成消耗
	private List<Items> composeCost = Lists.newArrayList();
	//升级消耗
	private List<Items> lvUpCost = Lists.newArrayList();

	private int lv1Stone = 1;//需要多少个1级宝石

	public void init(){
		for(String str:getAttr().split(",")){
			String[] strs = str.split(":");
			TankAttrType  attrType = TankAttrType.getType(Integer.parseInt(strs[0]));
			double value = Double.parseDouble(strs[1]);
			if(attrType!=null&&value>0){
				attrMap.put(attrType, value);
			}
		}
		this.composeCost = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		this.lvUpCost = ItemUtils.str2ItemList(this.getLevelup_cost(), ",", ":");
		if(getLevel() > 1) {
			this.lv1Stone = (int)Math.pow(getNext_need_count(),(getLevel()-1));
		}
	}
	
	public List<Items> getComposeCost(long count){
		return composeCost.stream().map(t-> {
			Items item = t.clone();
			item.setCount(item.getCount()*count);
			return item;
			}).collect(Collectors.toList());
	}
	public List<Items> getLvUpCost(){
		return getLvUpCost(1);
	}
	
	public List<Items> getLvUpCost(long num){
		return lvUpCost.stream().map(t -> {
			Items item = t.clone();
			item.setCount(item.getCount()*num);
			return item;
		}).collect(Collectors.toList());
	}

	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
}
