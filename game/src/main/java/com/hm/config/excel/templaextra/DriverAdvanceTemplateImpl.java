package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.DriverAdvanceTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("driver_advance")
public class DriverAdvanceTemplateImpl extends DriverAdvanceTemplate {
	Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	Map<TankAttrType, Double> attrMapSS = Maps.newHashMap();

	Map<TankAttrType, Double> fetterAttrMap = Maps.newHashMap();

	Map<Integer, List<Integer>> chooseAttr = Maps.newHashMap();
	Map<Integer, List<Integer>> chooseAttrSS = Maps.newHashMap();
	List<Items> lvCost = Lists.newArrayList();
	

	
	public void init(){
		if(!getAttri_base().isEmpty()){
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri_base(), ",", ":");
		}
		if(!getAttri_base_ss().isEmpty()){
			this.attrMapSS = TankAttrUtils.str2TankAttrMap(getAttri_base_ss(), ",", ":");
		}
		
		if(!getAttri_circle().isEmpty()){
			this.fetterAttrMap = TankAttrUtils.str2TankAttrMap(getAttri_circle(), ",", ":");
		}
		if(!getAttri_choose().isEmpty()){
			List<String> tempList = StringUtil.splitStr2StrList(getAttri_choose(), ";");
			for(int i=0; i<tempList.size(); i++) {
				chooseAttr.put(i+1, StringUtil.splitStr2IntegerList(tempList.get(i), ","));
			}
		}
		
		
		if(!getAttri_choose_ss().isEmpty()){
			List<String> tempList = StringUtil.splitStr2StrList(getAttri_choose_ss(), ";");
			for(int i=0; i<tempList.size(); i++) {
				chooseAttrSS.put(i+1, StringUtil.splitStr2IntegerList(tempList.get(i), ","));
			}
		}
		if(!getCost().isEmpty()){
			lvCost = ItemUtils.str2ItemList(getCost(), ",", ":");
		}
	}
	public Map<TankAttrType, Double> getAttr(int rare){
		return rare>=4?attrMapSS:attrMap; 
	}
	
	public List<Items> getLvCost() {
		return lvCost;
	}
	
	public boolean contains(int id, int tankType, int rare) {
		if(rare>=4) {
			return null!=chooseAttrSS.get(tankType)?chooseAttrSS.get(tankType).contains(id):false;
		}
		return null!=chooseAttr.get(tankType)?chooseAttr.get(tankType).contains(id):false;
	}
	
	public Map<TankAttrType, Double> getFetterAttrMap() {
		return fetterAttrMap;
	}
}





