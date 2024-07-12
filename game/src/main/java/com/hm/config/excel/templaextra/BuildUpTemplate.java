package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BuildingUpgradeTemplate;
import com.hm.enums.AttributeType;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;
@FileConfig("building_upgrade")
public class BuildUpTemplate extends BuildingUpgradeTemplate{
	private Map<AttributeType,Double> addMap = Maps.newConcurrentMap();
	private List<Items> costs = Lists.newArrayList();
	private List<Items> products = Lists.newArrayList();
	private List<List<Items>> productCosts = Lists.newArrayList();
	private List<Integer> productTimes = Lists.newArrayList();
	private List<Integer> productLimits = Lists.newArrayList();
	private long costCrystal;
	public void init() {
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		this.costCrystal = ItemUtils.getItemNum(costs,ItemType.CURRENCY.getType(),PlayerAssetEnum.Crystal.getTypeId());
		this.products = ItemUtils.str2ItemList(this.getProduct(), ",", ":");
		for(String str:this.getProduct_cost().split(";")){
			productCosts.add(ItemUtils.str2ItemList(str, ",", ":"));
		}
		this.productTimes = StringUtil.splitStr2IntegerList(this.getProduct_time(), ",");
		this.productLimits = StringUtil.splitStr2IntegerList(this.getProduct_limit(), ",");
		load(this.getBuilding_effect());
	}

	/**
	 * 1:2.1;第一个值代表属性 第二个代表值
	 * @param str 
	 */
	public void load(String building_effect) {
		if(StringUtil.isNullOrEmpty(building_effect)){
			return;
		}
		for (String s : building_effect.split(",")) {
			String[] info = s.split(":");
			AttributeType addType = AttributeType.getAddType(Integer.parseInt(info[0]));
			double value = Double.parseDouble(info[1]);//值
			addMap.put(addType, value);
		}
	}
	
	/**
	 *根据类型获得该等级的量 
	 */
	public double getByAddType(AttributeType type){
		return addMap.getOrDefault(type, 0d);
	}

	public List<Items> getCosts() {
		return costs;
	}

	public List<Items> getProducts() {
		return products;
	}

	public List<List<Items>> getProductCosts() {
		return productCosts;
	}

	public List<Items> getProductCost(int index) {
		return this.productCosts.get(index-1);
	}

	public Items getProduct(int index,int num) {
		Items clone = this.products.get(index-1).clone();
		clone.setCount(clone.getCount()*num);
		return clone;
	}
	
	public int getProductTime(int index) {
		return this.productTimes.get(index-1);
	}
	
	public int getProductLimit(int index) {
		return this.productLimits.get(index-1);
	}

	public long getCostCrystal() {
		return costCrystal;
	}
	
	
	
}
