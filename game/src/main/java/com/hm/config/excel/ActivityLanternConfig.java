package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active115GiftTemplate;
import com.hm.config.excel.templaextra.ActiveLanternConsumeGiftImpl;
import com.hm.config.excel.templaextra.ActiveLanternFormulaImpl;
import com.hm.config.excel.templaextra.ActiveLanternRandomShopImpl;
import com.hm.config.excel.templaextra.ActiveLanternShopImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @ClassName: ActivityLanternConfig 
 * @Description: 元宵节活动配置文件
 * @author zxj
 * @date 2019年12月21日 上午9:32:02 
 *
 */
@Config ( "ActivityLanternConfig" )
public class ActivityLanternConfig extends ExcleConfig{
	//合成月饼信息
	private Map<Integer, ActiveLanternFormulaImpl> formulaTemplate = Maps.newConcurrentMap();
	//充值统计--奖励
	private Map<Integer, ActiveLanternConsumeGiftImpl> giftTemplate = Maps.newConcurrentMap();
	//中秋好礼--购买礼包
	private Map<Integer, ActiveLanternShopImpl> buyGiftTemplate = Maps.newConcurrentMap();
	//那兔送福，购买礼包信息
	private Map<Integer, ActiveLanternRandomShopImpl> randomShop = Maps.newConcurrentMap();
	//那兔送福，计费点信息（计费点，对应的奖励）
	private Map<Integer, List<ActiveLanternRandomShopImpl>> randomShopMoney = Maps.newConcurrentMap();
	////那兔送福，计费点信息
	List<ActiveLanternRandomShopImpl> listRandomShop = Lists.newArrayList();

	//礼包，购买（rechargeid，obj）
	private Map<Integer, Active115GiftTemplate> rechargeGiftMap = Maps.newConcurrentMap();

	
	@Override
	public void loadConfig() {
//		loadFormulaTemplate();
//		loadGiftTemplate();
//		loadBuyGiftTemplate();
//		loadRandomShop();
//		loadRechargeGift();
	}
	   
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveLanternConsumeGiftImpl.class,
				ActiveLanternFormulaImpl.class,
				ActiveLanternRandomShopImpl.class,
				ActiveLanternShopImpl.class,
				Active115GiftTemplate.class);
	}

	/**
	 * getFormula:(根据id获取合成月饼信息). <br/>  
	 * @author zxj  
	 * @param id
	 * @return  使用说明
	 */
	public ActiveLanternFormulaImpl getFormula(int id) {
		return this.formulaTemplate.get(id);
	}
	/**
	 * getGift:(获取资源礼包信息). <br/>  
	 * @author zxj  
	 * @param id
	 * @return  使用说明
	 */
	public ActiveLanternConsumeGiftImpl getGift(int id) {
		return this.giftTemplate.get(id);
	}
	/**
	 * getBuyGift:(中秋好礼，购买礼包). <br/>  
	 * @author zxj  
	 * @param id
	 * @return  使用说明
	 *
	 */
	public ActiveLanternShopImpl getBuyGift(int id) {
		return this.buyGiftTemplate.get(id);
	}
	//根据id，获取随机礼包
	public ActiveLanternRandomShopImpl getRandomById(int id) {
		return this.randomShop.get(id);
	}
	//根据计费点，服务器等级，获取随机礼包
	public ActiveLanternRandomShopImpl getRandomByPrice(int priceId, int lv) {
		if(null==randomShopMoney.get(priceId)) {
			return null;
		}
		return randomShopMoney.get(priceId).stream().filter(e->{
			return e.checkLv(lv);
		}).findFirst().orElse(null);
	}
	//获取随机的随机礼包
	public ActiveLanternRandomShopImpl getRandomShop(int lv, int id) {
		Map<ActiveLanternRandomShopImpl, Integer> tempMap = listRandomShop.stream().filter(e->{
			return e.checkLv(lv) && e.getId()!=id;
		}).collect(Collectors.toMap(Function.identity(), e->1));
		WeightMeta<ActiveLanternRandomShopImpl> randomWeight = RandomUtils.buildWeightMeta(tempMap);
		return randomWeight.random();
	}
	
	//合成月饼信息
	private void loadFormulaTemplate() {
		Map<Integer, ActiveLanternFormulaImpl> tempFormulaTemplate = Maps.newConcurrentMap();
		for(ActiveLanternFormulaImpl template :JSONUtil.fromJson(getJson(ActiveLanternFormulaImpl.class),
				new TypeReference<ArrayList<ActiveLanternFormulaImpl>>() {
				})) {
			template.init();
			tempFormulaTemplate.put(template.getId(), template);
		}
		this.formulaTemplate  = ImmutableMap.copyOf(tempFormulaTemplate);
	}
	//获取资源礼包信息
	private void loadGiftTemplate() {
		Map<Integer, ActiveLanternConsumeGiftImpl> tempGiftTemplate = Maps.newConcurrentMap();
		for(ActiveLanternConsumeGiftImpl template :JSONUtil.fromJson(getJson(ActiveLanternConsumeGiftImpl.class),
				new TypeReference<ArrayList<ActiveLanternConsumeGiftImpl>>() {
				})) {
			template.init();
			tempGiftTemplate.put(template.getId(), template);
		}
		this.giftTemplate  = ImmutableMap.copyOf(tempGiftTemplate);
	}
	
	private void loadBuyGiftTemplate() {
		Map<Integer, ActiveLanternShopImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
		for(ActiveLanternShopImpl template :JSONUtil.fromJson(getJson(ActiveLanternShopImpl.class),
				new TypeReference<ArrayList<ActiveLanternShopImpl>>() {
				})) {
			template.init();
			tempBuyGiftTemplate.put(template.getId(), template);
		}
		this.buyGiftTemplate  = ImmutableMap.copyOf(tempBuyGiftTemplate);
	}
	
	//获取资源礼包信息
	private void loadRandomShop() {
		Map<Integer, ActiveLanternRandomShopImpl> tempTemplate = Maps.newConcurrentMap();
		
		for(ActiveLanternRandomShopImpl template :JSONUtil.fromJson(getJson(ActiveLanternRandomShopImpl.class),
				new TypeReference<ArrayList<ActiveLanternRandomShopImpl>>() {
				})) {
			template.init();
			tempTemplate.put(template.getId(), template);
			listRandomShop.add(template);
			if(template.getType()==1) {
				List<ActiveLanternRandomShopImpl> tempList = randomShopMoney.getOrDefault(Integer.parseInt(template.getGoods()), Lists.newArrayList());
				tempList.add(template);
				randomShopMoney.put(Integer.parseInt(template.getGoods()), tempList);
			}
		}
		this.randomShop  = ImmutableMap.copyOf(tempTemplate);
	}

	public void loadRechargeGift() {
		Map<Integer, Active115GiftTemplate> tempRechargeGift = Maps.newConcurrentMap();
		List<Active115GiftTemplate> tempList1 = JSONUtil.fromJson(getJson(Active115GiftTemplate.class), new TypeReference<List<Active115GiftTemplate>>() {
		});
		tempList1.forEach(e -> {
			tempRechargeGift.put(e.getRecharge_id(), e);
		});
		rechargeGiftMap = ImmutableMap.copyOf(tempRechargeGift);
	}

	public Active115GiftTemplate getRechargeGift(int rechargeId) {
		return rechargeGiftMap.getOrDefault(rechargeId, null);
	}
}




