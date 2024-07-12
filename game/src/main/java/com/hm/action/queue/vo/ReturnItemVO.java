package com.hm.action.queue.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;

import java.util.List;
import java.util.Map;

public class ReturnItemVO {
	private Map<CurrencyKind,Long> returnResMap = Maps.newConcurrentMap();
	private List<Items> returnItems = Lists.newArrayList();
	public Map<CurrencyKind, Long> getReturnResMap() {
		return returnResMap;
	}
	public void setReturnResMap(Map<CurrencyKind, Long> returnResMap) {
		this.returnResMap = returnResMap;
	}
	public List<Items> getReturnItems() {
		return returnItems;
	}
	public void setReturnItems(List<Items> returnItems) {
		this.returnItems = returnItems;
	}
	
	

}
