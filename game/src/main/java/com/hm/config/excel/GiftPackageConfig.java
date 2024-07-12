package com.hm.config.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.GiftPackageTemplate;
import com.hm.config.excel.templaextra.ActiveGiftDayTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Config
public class GiftPackageConfig extends ExcleConfig {
	//新手礼包
	private Map<Integer, GiftPackageTemplate> map = Maps.newConcurrentMap();


	//====================周日月礼包=====================
	private Map<Integer, ActiveGiftDayTemplateImpl> giftMap = Maps.newConcurrentMap();
	//key:计费点id  val:礼包id
	private Map<Integer, ActiveGiftDayTemplateImpl> rechargeMap = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		this.map = json2ImmutableMap(GiftPackageTemplate::getGift_id,GiftPackageTemplate.class);

		this.giftMap = json2ImmutableMap(ActiveGiftDayTemplateImpl::getId,ActiveGiftDayTemplateImpl.class);
		this.rechargeMap = this.giftMap.values().stream().filter(e -> e.getRecharge_gift_id() > 0)
				.collect(Collectors.toMap(ActiveGiftDayTemplateImpl::getRecharge_gift_id, e->e));
	}

	public GiftPackageTemplate getGiftPackageTemplateById(int id) {
		return map.get(id);
	}


	public List<Items> rewardGiftList(int giftId) {
		GiftPackageTemplate giftPackage = getGiftPackageTemplateById(giftId);
		if(giftPackage != null) {
			return ItemUtils.createCloneItems(giftPackage.getItemList());
		}
		return Lists.newArrayList();
	}


	public ActiveGiftDayTemplateImpl getTemplateByRechargeId(int rechargeGifId) {
		return this.rechargeMap.get(rechargeGifId);
	}
	public ActiveGiftDayTemplateImpl getTemplate(int giftId) {
		return this.giftMap.get(giftId);
	}
}
  
