package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ChipTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.setting.TankSetting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FileConfig("chip")
public class ChipTemplateImpl extends ChipTemplate{
	//升级消耗
	private List<Items> cost = Lists.newArrayList();
	//当前位置的科技的，当前属性加成
	private TankAttr tankAttr = new TankAttr();
	
	private List<Integer> nextTech = Lists.newArrayList();
	
	public void init(){
		this.cost = ItemUtils.str2ItemList(this.getBill(), ",", ":");
		double[] attrArr = StringUtil.strToDoubleArray(this.getValue(), ",");
		if(attrArr.length==2) {
			tankAttr.addAttr(new Double(attrArr[0]).intValue(), attrArr[1]);
		}
		nextTech = StringUtil.splitStr2IntegerList(this.getNext_tec(), ",");
	}
	//根据等级获取属性加成
	public TankAttr getTankAttr(int lv) {
		TankAttr tempAttr = new TankAttr();
		if(lv<=0) {
			return tempAttr;
		}
		if(lv>this.getMax_level()) {
			lv = this.getMax_level();
		}
		Map<TankAttrType,Double> tempMap = this.tankAttr.getAttrMap();
		for (TankAttrType key : tempMap.keySet()) { 
			tempAttr.addAttr(key, tempMap.get(key)*lv);
		} 
		return tempAttr;
	}
	//获取升级消耗
	public List<Items> getCost(TankSetting tankSetting) {
		List<Items> tempItems = this.cost.stream().map(t ->t.clone()).collect(Collectors.toList());
		return tempItems;
	}
	//获取升级图纸消耗
	public Items getPaperCost(TankSetting tankSetting) {
		return new Items(tankSetting.getPaper_id(), this.getFragment() ,ItemType.PAPER.getType());
	}
	
	public boolean checkTankLimit(Tank tank) {
		return tank.getLv()>=this.getLevel() && tank.getStar()>=this.getStar();
	}
	
	public boolean nextTechContains(int position) {
		return nextTech.contains(position);
	}
	
}








