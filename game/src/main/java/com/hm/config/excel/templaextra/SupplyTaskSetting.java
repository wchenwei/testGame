package com.hm.config.excel.templaextra;

import com.hm.libcore.util.string.StringUtil;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;
import lombok.Data;

import java.util.List;

@Data
public class SupplyTaskSetting {
	private Items item;
	private int star;
	private int weight;
	private int waySize;
	private int low;
	private int top;
	
	public SupplyTaskSetting(String info) {
		List<Integer> parms = StringUtil.splitStr2IntegerList(info, ":");
		this.star = parms.get(0);
		this.weight = parms.get(1);
		this.waySize = parms.get(2);
		this.low = parms.get(3);
		this.top = parms.get(4);
	}
	
	public Items buildItems() {
		Items clones = item.clone();
		if(low >= top) {
			clones.setCount(top);
		}else{
			clones.setCount(RandomUtils.randomInt(low, top));
		}
		return clones;
	}
}
