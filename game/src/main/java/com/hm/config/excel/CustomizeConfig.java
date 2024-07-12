package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.templaextra.ActivePrivateGiftImpl;
import com.hm.config.excel.templaextra.ActivePrivateGiftItemImpl;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName:  CustomizeConfig   
 * @Description: 私人定制，充值礼包
 * @author: zxj
 * @date:   2020年5月12日 下午2:24:57
 */
@Config
public class CustomizeConfig extends ExcleConfig {

	Map<Integer, ActivePrivateGiftItemImpl> privateGiftItemMap = Maps.newConcurrentMap();
	Map<Integer, ActivePrivateGiftImpl> privateGiftMap = Maps.newConcurrentMap();
	Set<Integer> rechargeIds = Sets.newHashSet();
	
	@Override
	public void loadConfig() {
		loadPrivateGiftItem();
		loadPrivateGift();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActivePrivateGiftItemImpl.class,
				ActivePrivateGiftImpl.class);
	}
	private void loadPrivateGiftItem() {
		Map<Integer, ActivePrivateGiftItemImpl> tempPrivateGiftItem = Maps.newConcurrentMap();
		for(ActivePrivateGiftItemImpl template :JSONUtil.fromJson(getJson(ActivePrivateGiftItemImpl.class),
				new TypeReference<ArrayList<ActivePrivateGiftItemImpl>>() {
				})) {
			template.init();
			tempPrivateGiftItem.put(template.getId(), template);
		}
		this.privateGiftItemMap  = ImmutableMap.copyOf(tempPrivateGiftItem);
	}
	
	private void loadPrivateGift() {
		Map<Integer, ActivePrivateGiftImpl> tempPrivateGift = Maps.newConcurrentMap();
		Set<Integer> tempRechargeIds = Sets.newHashSet();
		for(ActivePrivateGiftImpl template :JSONUtil.fromJson(getJson(ActivePrivateGiftImpl.class),
				new TypeReference<ArrayList<ActivePrivateGiftImpl>>() {
				})) {
			template.init();
			tempRechargeIds.add(template.getRecharge_gift());
			tempPrivateGift.put(template.getId(), template);
		}
		this.privateGiftMap  = ImmutableMap.copyOf(tempPrivateGift);
		this.rechargeIds = ImmutableSet.copyOf(tempRechargeIds);
	}
	/**
	 * @Title: checkData   
	 * @Description: 校验等级，跟位置，以及位置是否包含此类型
	 * @param: @param playerLv
	 * @param: @param id
	 * @param: @param index
	 * @param: @param type
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public boolean checkData(int playerLv, int id, int index, int type) {
		ActivePrivateGiftImpl privateGift = privateGiftMap.get(id);
		return privateGift.fitLv(playerLv) && privateGift.checkData(index, type);
	}
	//校验计费点，是否是活动中的计费点
	public boolean checkRechargeId(int rechargeId) {
		return rechargeIds.contains(rechargeId);
	}
	
	public List<Items> getRewards(int id) {
		List<Items> result = Lists.newArrayList();
		if(!privateGiftItemMap.containsKey(id)) {
			return result;
		}
 		return privateGiftItemMap.get(id).getRewardItems();
	} 
}









