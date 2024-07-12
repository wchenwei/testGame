package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankSpecialityTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FileConfig("tank_speciality")
public class TankSpecTempImpl extends TankSpecialityTemplate{
	private List<Items> costList = Lists.newArrayList();
	private TankAttr tankAttr = new TankAttr();
	private List<Integer> skillList = Lists.newArrayList();
	//重铸返还
	private List<Items> recasetItems = Lists.newArrayList();
	
	public void init(Table<Integer, Integer, TankSpecTempImpl> tankSpecTable) {
		this.costList = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		Map<Integer, TankSpecTempImpl> treeData = tankSpecTable.row(this.getTree());
		treeData.values().stream().sorted(Comparator.comparing(TankSpecialityTemplate::getLevel).reversed())
			.filter(e->e.getLevel()<=this.getLevel()).forEach(e->{
			if(e.getSkill_id()>0 && skillList.size() < 3) { //保留最后三个技能
				this.skillList.add(e.getSkill_id());
			}
			if(!StringUtil.isNullOrEmpty(e.getAttribute())) {
				double[] attr =StringUtil.strToDoubleArray(e.getAttribute(), ":");
				tankAttr.addAttr(new Double(attr[0]).intValue(), attr[1]);
			}
		});
	}
	public void initRecasetItems(Table<Integer, Integer, TankSpecTempImpl> tankSpecTable) {
		Map<Integer, TankSpecTempImpl> treeData = tankSpecTable.row(this.getTree());
		List<Items> itemList = treeData.values().stream().filter(e->e.getLevel()<=this.getLevel())
		.flatMap(e -> e.getCostList().stream())
		//不算钞票
		.filter(e -> !(e.getType() == ItemType.CURRENCY.getType() && e.getType() == PlayerAssetEnum.Cash.getTypeId()))
		.collect(Collectors.toList());
		this.recasetItems = ItemUtils.mergeItemList(itemList);
	}
	
	public List<Items> getCostList() {
		return costList;
	}

	public TankAttr getTankAttr() {
		return tankAttr;
	}

	public List<Integer> getSkillList() {
		return skillList;
	}

	public List<Items> getRecasetItems() {
		return recasetItems;
	}

}





