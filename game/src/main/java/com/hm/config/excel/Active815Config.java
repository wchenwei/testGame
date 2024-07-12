package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.Active815FormulaImpl;
import com.hm.config.excel.templaextra.Active815GiftImpl;
import com.hm.config.excel.templaextra.Active815RandomShopImpl;
import com.hm.config.excel.templaextra.Active815ShopImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: Active815Config. <br/>  
 * Function: 中秋节活动配置文件. <br/>  
 * date: 2019年8月5日 上午11:05:37 <br/>  
 * @author zxj  
 * @version
 */
@Config ( "Active815Config" )
public class Active815Config extends ExcleConfig{
	//合成月饼信息
	private Map<Integer, Active815FormulaImpl> formulaTemplate = Maps.newConcurrentMap();
	//充值统计--奖励
	private Map<Integer, Active815GiftImpl> giftTemplate = Maps.newConcurrentMap();
	//中秋好礼--购买礼包
	private Map<Integer, Active815ShopImpl> buyGiftTemplate = Maps.newConcurrentMap();
	//那兔送福，购买礼包信息
	private Map<Integer, Active815RandomShopImpl> randomShop = Maps.newConcurrentMap();
	//那兔送福，计费点信息（计费点，对应的奖励）
	private Map<Integer, List<Active815RandomShopImpl>> randomShopMoney = Maps.newConcurrentMap();
	////那兔送福，计费点信息
	List<Active815RandomShopImpl> listRandomShop = Lists.newArrayList();

	
	@Override
	public void loadConfig() {
//		loadFormulaTemplate();
//		loadGiftTemplate();
//		loadBuyGiftTemplate();
//		loadRandomShop();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(Active815GiftImpl.class,
				Active815FormulaImpl.class,
				Active815RandomShopImpl.class,
				Active815ShopImpl.class);
	}

	/**
	 * getFormula:(根据id获取合成月饼信息). <br/>  
	 * @author zxj  
	 * @param id
	 * @return  使用说明
	 */
	public Active815FormulaImpl getFormula(int id) {
		return this.formulaTemplate.get(id);
	}
	/**
	 * getGift:(获取资源礼包信息). <br/>  
	 * @author zxj  
	 * @param id
	 * @return  使用说明
	 */
	public Active815GiftImpl getGift(int id) {
		return this.giftTemplate.get(id);
	}
	/**
	 * getBuyGift:(中秋好礼，购买礼包). <br/>  
	 * @author zxj  
	 * @param id
	 * @return  使用说明
	 *
	 */
	public Active815ShopImpl getBuyGift(int id) {
		return this.buyGiftTemplate.get(id);
	}
	//根据id，获取随机礼包
	public Active815RandomShopImpl getRandomById(int id) {
		return this.randomShop.get(id);
	}
	//根据计费点，服务器等级，获取随机礼包
	public Active815RandomShopImpl getRandomByPrice(int priceId, int lv) {
		if(null==randomShopMoney.get(priceId)) {
			return null;
		}
		return randomShopMoney.get(priceId).stream().filter(e->{
			return e.checkLv(lv);
		}).findFirst().orElse(null);
	}
	//获取随机的随机礼包
	public Active815RandomShopImpl getRandomShop(int lv, int id) {
		Map<Active815RandomShopImpl, Integer> tempMap = listRandomShop.stream().filter(e->{
			return e.checkLv(lv) && e.getId()!=id;
		}).collect(Collectors.toMap(Function.identity(), e->1));
		WeightMeta<Active815RandomShopImpl> randomWeight = RandomUtils.buildWeightMeta(tempMap);
		return randomWeight.random();
	}
	
	//合成月饼信息
	private void loadFormulaTemplate() {
		Map<Integer, Active815FormulaImpl> tempFormulaTemplate = Maps.newConcurrentMap();
		for(Active815FormulaImpl template :JSONUtil.fromJson(getJson(Active815FormulaImpl.class),
				new TypeReference<ArrayList<Active815FormulaImpl>>() {
				})) {
			template.init();
			tempFormulaTemplate.put(template.getId(), template);
		}
		this.formulaTemplate  = ImmutableMap.copyOf(tempFormulaTemplate);
	}
	//获取资源礼包信息
	private void loadGiftTemplate() {
		Map<Integer, Active815GiftImpl> tempGiftTemplate = Maps.newConcurrentMap();
		for(Active815GiftImpl template :JSONUtil.fromJson(getJson(Active815GiftImpl.class),
				new TypeReference<ArrayList<Active815GiftImpl>>() {
				})) {
			template.init();
			tempGiftTemplate.put(template.getId(), template);
		}
		this.giftTemplate  = ImmutableMap.copyOf(tempGiftTemplate);
	}
	
	private void loadBuyGiftTemplate() {
		Map<Integer, Active815ShopImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
		for(Active815ShopImpl template :JSONUtil.fromJson(getJson(Active815ShopImpl.class),
				new TypeReference<ArrayList<Active815ShopImpl>>() {
				})) {
			template.init();
			tempBuyGiftTemplate.put(template.getId(), template);
		}
		this.buyGiftTemplate  = ImmutableMap.copyOf(tempBuyGiftTemplate);
	}
	
	//获取资源礼包信息
	private void loadRandomShop() {
		Map<Integer, Active815RandomShopImpl> tempTemplate = Maps.newConcurrentMap();
		
		for(Active815RandomShopImpl template :JSONUtil.fromJson(getJson(Active815RandomShopImpl.class),
				new TypeReference<ArrayList<Active815RandomShopImpl>>() {
				})) {
			template.init();
			tempTemplate.put(template.getId(), template);
			listRandomShop.add(template);
			if(template.getType()==1) {
				List<Active815RandomShopImpl> tempList = randomShopMoney.getOrDefault(Integer.parseInt(template.getGoods()), Lists.newArrayList());
				tempList.add(template);
				randomShopMoney.put(Integer.parseInt(template.getGoods()), tempList);
			}
		}
		this.randomShop  = ImmutableMap.copyOf(tempTemplate);
	}
}




