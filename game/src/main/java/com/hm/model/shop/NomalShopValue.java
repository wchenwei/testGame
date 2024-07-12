package com.hm.model.shop;

import com.hm.enums.ShopType;
/**
 * 正常商店，不限购
 * @author xjt
 *
 */
public class NomalShopValue extends PlayerShopValue {
	public NomalShopValue(ShopType shopType){
		super(shopType);
	}
	
	public NomalShopValue() {
		super();
	}
	
}
