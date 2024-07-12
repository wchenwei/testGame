package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.FettersTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;
import com.hm.util.ItemUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * ClassName: FettersImpl. <br/>  
 * Function: 坦克羁绊配置文件. <br/>  
 * date: 2019年6月11日 上午10:58:49 <br/>  
 * @author zxj  
 * @version
 */
@FileConfig("fetters")
public class FettersImpl extends FettersTemplate {
	private List<Integer> tankList;
	private List<Integer> starList;
	private List<Items> itemList;
	private List<Map<TankAttrType, Double>> attrAdd = Lists.newArrayList();
	private int maxLv = 0;
	private String idStr = null;
	
	public void init() {
		this.tankList = StringUtil.splitStr2IntegerList(this.getFriend_id(), ",")
				.stream().filter(e -> e > 0).collect(Collectors.toList());
		
		this.starList = StringUtil.splitStr2IntegerList(this.getStar_num(), ",");
		Collections.sort(this.starList);
		this.idStr = StringUtil.list2Str(tankList, ",");
		maxLv = starList.size();
		this.itemList = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		List<String> attrList = StringUtil.splitStr2StrList(this.getAttr_value(), ",");
		attrList.stream().forEach(e->{
			String[] attr = e.split(":");
			Map<TankAttrType, Double> tempMap = Maps.newHashMap();
			tempMap.put(TankAttrType.getType(Integer.parseInt(attr[0])), Double.parseDouble(attr[1]));
			attrAdd.add(tempMap);
		});
	}
	
	public Items getCostItem(int lv) {
		if(lv>this.itemList.size()) {
			return null;
		}
		return this.itemList.get(lv-1).clone();
	}
	public int getStarNum(int lv) {
		return this.starList.get(lv-1);
	}

	public int getMaxLv() {
		return maxLv;
	}

	public List<Integer> getTankList() {
		return tankList;
	}
	
	public TankAttr getAttrByLv(int lv) {
		TankAttr tankAttr = new TankAttr();
		for(int i=0;i<lv;i++){
			tankAttr.addAttr(this.attrAdd.get(i));
		}
		return tankAttr;
	}

	public String getIdStr() {
		return this.idStr;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}

	@Override
	public boolean equals(Object obj) {
		return this.getId() == ((FettersImpl)obj).getId();
	}
}



