package com.hm.model.shop;


import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.ShopGoodsExtraTemplate;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.enums.ShopType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.util.MathUtils;

import java.util.List;

public abstract class PlayerShopValue {
	private int shopId;
	private long nextRestTime;//下一次重置时间
	private long endTime;//商店消失时间
	
	public PlayerShopValue() {
		super();
	}
	public PlayerShopValue(ShopType shopType) {
		super();
		this.shopId = shopType.getType();
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public long getNextRestTime() {
		return nextRestTime;
	}
	public void setNextRestTime(long nextRestTime) {
		this.nextRestTime = nextRestTime;
	}
	//只判断商品还有没有库存
	public boolean isCanBuy(Player player, ShopItemExtraTemplate template, int num) {
		return true;
	}

	//设置购买数据
	public void setShopBuyData(Player player, int id, int num) {
	}
	
	//此商店是否可以刷新
	public boolean isCanRefersh(){
		return false;
	}
	//刷新消耗
	public Items getRefershCost() {
		return null;
	}
	//刷新商店
	public void refresh(BasePlayer player){
		
	}
	// 无消耗刷新一次商店
	public void refreshOnce(BasePlayer player){

	}

	//商店每日重置
	public void resetDay(BasePlayer player) {
	}
	//商店每日重置
	public void reset(BasePlayer player) {
		
	}
	//解锁商店
	public void unlockShop(BasePlayer context) {
		
	}
	//重复解锁
	public void unlockRepeat(Player player,String param){
	}
	//此商店是否可以刷新
	public boolean isTimeOut(){
		return this.endTime>0&&this.endTime<System.currentTimeMillis();
	}
	/**
	 * 获取对应的商品
	 * @param goodId
	 * @param num
	 * @return
	 */
	public Items getReward(int goodId, int num){
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ShopGoodsExtraTemplate template = shopConfig.getGoodsTemplate(goodId);
		Items reward = template.getReward();
		reward.setCount(reward.getCount() * num);
		return reward;
	}
	/**
	 * 获取唯一id对应的商品，一般只对应一个固定的goodsId，不固定的从多个gooodsId中随机的从各自子类中获取
	 * @param id 唯一id
	 * @return
	 */
	public int getGoodsId(int id) {
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ShopItemExtraTemplate template = shopConfig.getShopTemplate(id);
		return Integer.parseInt(template.getGoods());
	}
	/**
	 * 获取购买消耗
	 * @param id 唯一id
	 * @param goodsId 对应的商品id
	 * @param num
	 * @return
	 */
	public List<Items> getCost(int id, int goodsId, long num){
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ShopGoodsExtraTemplate goodsTemplate = shopConfig.getGoodsTemplate(goodsId);
		if(goodsTemplate==null){
			return null;
		}
		List<Items> costItem = goodsTemplate.getCost();
		int discount = getShopDiscount(id);//折扣比例
		costItem.forEach(t->{
			int count = (int) MathUtils.div(MathUtils.mul(t.getCount(), discount), 100);
			t.setCount(count * num);
		});
		return costItem;
	}
	/**
	 * 获取折扣比例
	 * @param id 唯一id
	 * @return
	 */
	public int getShopDiscount(int id) {
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ShopItemExtraTemplate template = shopConfig.getShopTemplate(id);
		if(template==null){
			return 100;
		}
		return template.getDiscount();
	}
	
	public boolean checkReset(BasePlayer player){
		if(isTimeOut()){
			return false;
		}
		if(this.nextRestTime==0){
			this.nextRestTime = TimeUtils.getNowZero();
		}
		if(System.currentTimeMillis()>=this.nextRestTime){
			reset(player);
			this.nextRestTime = calNextResetTime();
			return true;
		}
		return false;
	}
	//计算下一次重置次数时间
	public long calNextResetTime(){
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		List<Integer> resetHour = shopConfig.getShopRestHour(this.getShopId());
		int curHour = DateUtil.getHour();
		for(int hour:resetHour){
			if(hour>curHour){
				return DateUtil.getAppointTime(hour, 0, 0);
			}
		}
		//如果没有找到则置为下一天0点
		return TimeUtils.getNowZero()+24*GameConstants.HOUR;
	}
	
}
