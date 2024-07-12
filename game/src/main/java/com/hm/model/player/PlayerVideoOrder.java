package com.hm.model.player;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class PlayerVideoOrder extends PlayerDataContext {
	//订单
	private ArrayList<String> orderIds = Lists.newArrayList();
	//是否已经使用过该订单
	public boolean isContainsOrder(String id) {
		return orderIds.contains(id);
	}
	
	
	public List<String> getOrderIds(){
		return orderIds;
	}
	
	public void addOrderId(String id){
		if(orderIds.size() >=50){
			orderIds.remove(orderIds.size()-1);
		}
		orderIds.add(0,id);
		SetChanged();
	}
}
