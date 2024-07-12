package com.hm.config.excel.temlate;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.enums.VipPowType;

import java.util.Map;

@FileConfig("shop_vip")
public class VipTemplate extends ShopVipTemplate{
	private Map<VipPowType,Integer> vipPowMap = Maps.newConcurrentMap();
	private int giftPrice;
	
	public void init() {
		for (String temp : getVip_power().split(",")) {
			VipPowType type = VipPowType.getVipPowType(Integer.parseInt(temp.split(":")[0]));
			if(type != null) {
				vipPowMap.put(type, Integer.parseInt(temp.split(":")[1]));
			}
		}
		if(getLv_vip() > 0) {
			this.giftPrice = Integer.parseInt(getGift_price().split(",")[1]);
		}
	}
	
	public int getVipPow(VipPowType type) {
		return vipPowMap.getOrDefault(type, 0);
	}
	
	public int getGiftPrice(int giftId) {
		if(giftId == getFree_gift()) {
			return 0;
		}
		return giftPrice;
	}
	
	public boolean isCanBuyGift(int giftId) {
		return giftId == getFree_gift() || giftId == getGift();
	}
}
