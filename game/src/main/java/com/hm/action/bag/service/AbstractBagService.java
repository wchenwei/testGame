package com.hm.action.bag.service;

import com.hm.model.item.Items;

import java.util.List;

public abstract class AbstractBagService {
	public abstract boolean isCanSell(int itemId);
	public abstract List<Items> sellReward(int itemId,long count);

}
