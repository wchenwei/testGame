/**  
 * Project Name:SLG_GameFuture.
 * File Name:MailBiz.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午5:23:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.action.commander.biz;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Sets;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankAttrBiz;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.CarTemplate;
import com.hm.config.excel.templaextra.MilitaryExtraTemplate;
import com.hm.config.excel.templaextra.MilitaryProjectLevelImpl;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.shop.PlayerShopValue;
import com.hm.model.tank.TankAttr;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.NumericalConstant;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


/**  
 * ClassName: commanderBiz. <br/>  
 * date: 2017年12月18日 下午5:23:27 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Biz
public class CommanderBiz{
	@Resource
	private CommanderConfig commanderConfig; 
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private ShopConfig shopConfig;
	@Resource
	private TankAttrBiz tankAttrBiz;
	
	/**
	 * 座驾升级,点击一次
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public boolean carlvUp(Player player,boolean isBuy){
		int lv = player.playerCommander().getCarLv(); 
		if(lv >= commanderConfig.getCarLvMax()){
			player.sendErrorMsg(SysConstant.Car_Lv_Max);
			return false; 
		}
		CarTemplate temp = commanderConfig.getCarTemplate(lv);
		Items costItems = temp.getCostItems(); 
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems,LogType.CarLvUp)){
			if(isBuy){
				if(!buyItems(player, ItemConstant.Car_Parts_ShopIndex,costItems.getCount(),LogType.CarLvUp)){
					return false; 
				}
			}else{
				player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
				return false; 
			}
			
		}
		int clickCount = player.playerCommander().getCarClickCount(); 
		//当前概率
		double nowRate = temp.getUp_rate()+clickCount * temp.getRate_add(); 
		//随机概率
		double randomRate = RandomUtil.randomDouble(0, 1);
		boolean isChange = false; //是否升级或升格
		//直接升级
		if(randomRate <=nowRate){
			player.playerCommander().addCarLv();
			isChange = true; 
		}else{
			//升级需要的点击次数
			int needCount = temp.getCount() * NumericalConstant.CarStageNum; 
			//直接升级
			if((clickCount+1) >= needCount){
				player.playerCommander().addCarLv();
				isChange = true; 
			}else{ //只增加点击次数
				player.playerCommander().addCarClickCount(); 
				//判断是否升格
				clickCount = player.playerCommander().getCarClickCount(); 
				if((clickCount % temp.getCount())== 0){
					isChange = true; 
				}else{
					isChange = false; 
				}
			}
		}
		//TODO 在监听器里检查可以解锁的技能
		//解锁技能
//		if(player.playerCommander().getCarLv() >lv){
//			int index = commanderConfig.getCarTemplate(player.playerCommander().getCarLv()).getMain_skill();
//			if(index >0){
//				player.playerCommander().unlockSkill(index-1);
//			}
//		}
		player.notifyObservers(ObservableEnum.CHCommancerDriverLvUpEvent, lv, player.playerCommander().getMilitaryLv(), costItems);
		//通知其他模块
		if(isChange){
			player.notifyObservers(ObservableEnum.CarLv,lv < player.playerCommander().getCarLv());
		}
		return true; 
	}
	
	/**
	 * 军衔升级
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public boolean militarylvUp(Player player,boolean isBuy){
		int lv = player.playerCommander().getMilitaryLv(); 
		if(lv >= commanderConfig.getMilitaryLvMax()){
			player.sendErrorMsg(SysConstant.Military_Lv_Max);
			return false; 
		}
		MilitaryExtraTemplate temp = commanderConfig.getMilitaryExtraTemplate(lv);
		Items costItems = temp.getCostItems(); 
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems,LogType.MilitaryLvUp)){
			if(isBuy){
				if(!buyItems(player, ItemConstant.Military_ShopIndex,costItems.getCount(),LogType.MilitaryLvUp)){
					return false; 
				}
			}else{
				player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
				return false; 
			}
		}
		int clickCount = player.playerCommander().getMilitaryClickCount(); 
		//当前概率
		double nowRate = temp.getUp_rate()+clickCount * temp.getRate_add(); 
		//随机概率
		double randomRate = RandomUtil.randomDouble(0, 1);
		boolean isChange = false; //是否升级或升格
		//直接升级
		if(randomRate <=nowRate){
			player.playerCommander().addMilitaryLv();
			isChange = true; 
		}else{
			//升级需要的点击次数
			int needCount = temp.getCount() * NumericalConstant.MilitaryStageNum; 
			//直接升级
			if((clickCount+1) >= needCount){
				player.playerCommander().addMilitaryLv();
				isChange = true; 
			}else{ //只增加点击次数
				player.playerCommander().addMilitaryClickCount(); 
				//判断是否升格
				clickCount = player.playerCommander().getMilitaryClickCount(); 
				if((clickCount % temp.getCount())== 0){
					isChange = true; 
				}else{
					isChange = false; 
				}
			}
		}
		boolean isLvUp = lv < player.playerCommander().getMilitaryLv();
		player.notifyObservers(ObservableEnum.MilitaryLvChange,isLvUp, isChange);
		if(isLvUp) {
			player.notifyObservers(ObservableEnum.MilitaryLvUp);
		}
		return true; 
	}

	
	/**
	 * 购买商品
	 *
	 * @author yanpeng 
	 * @param player
	 * @param itemId
	 * @return  
	 *
	 */
	private boolean buyItems(Player player, int shopIndex,long num, LogType logType) {
		ShopItemExtraTemplate template = shopConfig.getShopTemplate(shopIndex);
		int shopId = template.getShop_id();
		PlayerShopValue playerShopValue = player.playerShop().getPlayerShop(shopId);
		int goodsId = playerShopValue.getGoodsId(shopIndex);
		List<Items> cost = playerShopValue.getCost(shopIndex, goodsId, num);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, logType.value(shopId))) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return false;
		}
		return true;
	}
	
	/**
	 * 座驾场内技能
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public Set<Integer> getCarWarSkillList(Player player){
		Set<Integer> skillList = Sets.newHashSet();
		if(player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mounts)){
			int carLv = player.playerCommander().getCarLv(); 
			for(int i=1;i<=carLv;i++){
				skillList.add(commanderConfig.getCarTemplate(i).getCar_skill());
			}
		}
		return skillList; 
	}
	
	public void buySuperWeapon(Player player){
		player.playerCommander().buySuperWeapon();
		player.notifyObservers(ObservableEnum.SuperWeaponLv);
	}
	
	public double getCarHpAddRate(Player player){
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mounts)){
			return 0;
		}
		int lv = player.playerCommander().getCarLv();
		CarTemplate template = commanderConfig.getCarTemplate(lv);
		return template==null?0:template.getHp_recover();
	}
	/**
	 * getMilitaryProjectAttr:(获取指挥官--军工属性加成). <br/>  
	 * @author zxj  
	 * @param player
	 * @return  使用说明
	 */
	public TankAttr getMilitaryProjectAttr(Player player) {
        MilitaryProjectLevelImpl militarProject = commanderConfig.getMilitaryProject(player.playerCommander().getMilitaryProject().getLv());
        if(null!=militarProject) {
        	return militarProject.getTankAttr();
        }
        return null;
    }
}  
