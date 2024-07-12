package com.hm.config.excel.templaextra;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerArmEnhanceTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;

@FileConfig("player_arm_enhance")
public class PlayerArmStrengthenTemplate extends PlayerArmEnhanceTemplate{
	private Table<Integer,TankAttrType,Double> table = HashBasedTable.create();
	private List<Items> cost = Lists.newArrayList();
	public void init(){
		for(String str:getArm_attr().split(",")){
			String[] strs = str.split(":");
			int id = Integer.parseInt(strs[0]);
			TankAttrType  attrType = TankAttrType.getType(Integer.parseInt(strs[1]));
			double value = Double.parseDouble(strs[2]);
			if(attrType!=null&&value>0){
				table.put(id, attrType, value);
			}
		}
		this.cost = ItemUtils.str2ItemList(this.getCost_item(), ",", ":");
	}
	
	public List<Items> getCost(){
		return cost;
	}
	
	public Map<TankAttrType,Double> getAttrByBody(int bodyId){
		return table.row(bodyId);
	}
}
