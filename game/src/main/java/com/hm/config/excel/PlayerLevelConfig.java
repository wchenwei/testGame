/**  
 * Project Name:SLG_GameFuture.  
 * File Name:PlayerLvelConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2017年12月28日下午5:11:16  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.temlate.PlayerLevelTemplate;
import com.hm.config.excel.templaextra.PlayerLevelExtraTemplate;
import com.hm.config.excel.templaextra.PlayerLvCompensateTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.enums.TreasuryCollectType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * ClassName:PlayerLvelConfig <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月28日 下午5:11:16 <br/>  
 * @author   zqh  
 * @version  1.1  
 * @since    
 */
@Slf4j
@Config
public class PlayerLevelConfig extends ExcleConfig {
	private PlayerLevelExtraTemplate[] levelTemplates;
	private int maxLv;
	private Map<Integer,PlayerLvCompensateTemplate> lvCompensateMap = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		List<PlayerLevelExtraTemplate> tempList = loadFile();
		PlayerLevelExtraTemplate[] levelTemplates = new PlayerLevelExtraTemplate[tempList.size()];
		for (int i = 0; i < levelTemplates.length; i++) {
			PlayerLevelExtraTemplate template = tempList.get(i);
			template.init();
			levelTemplates[template.getLevel()-1] = template;
		}
		this.levelTemplates = levelTemplates;
		this.maxLv = levelTemplates[levelTemplates.length-1].getLevel();

		try {
			//加载补偿配置
//			loadLvCompensate();
		} catch (Exception e) {
			log.error("等级补偿加载出错",e);
		}
	}

	public void loadLvCompensate() {
		Map<Integer, PlayerLvCompensateTemplate> map = Maps.newConcurrentMap();
		for (PlayerLvCompensateTemplate template : JSONUtil.fromJson(getJson(PlayerLvCompensateTemplate.class), new TypeReference<ArrayList<PlayerLvCompensateTemplate>>(){})) {
			template.init();
			for (int i = template.getPlayer_lv_down(); i <= template.getPlayer_lv_up(); i++) {
				map.put(i, template);
			}
		}
		this.lvCompensateMap = ImmutableMap.copyOf(map);
	}
	
	private List<PlayerLevelExtraTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(PlayerLevelExtraTemplate.class), new TypeReference<ArrayList<PlayerLevelExtraTemplate>>(){});
	}
	
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(PlayerLevelExtraTemplate.class,PlayerLvCompensateTemplate.class);
	}
	
	public int getMaxLv() {
		return this.maxLv;
	}
	/**
	 * 获取等下升级到下级所需经验
	 * @param lv
	 * @return
	 */
	public long getLevelExp(int lv) {
		if(lv >= getMaxLv()) {
			return 0;
		}
		return getPlayerLevelTemplate(lv).getExp();
	}
	public long getLevelTotalExp(int lv){
		if(lv >= getMaxLv()) {
			return getPlayerLevelTemplate(maxLv).getExp_totle();
		}
		return getPlayerLevelTemplate(lv).getExp_totle();
	}
	
	
	public PlayerLevelExtraTemplate getPlayerLevelTemplate(BasePlayer player) {
		return getPlayerLevelTemplate(player.playerCommander().getMilitaryLv());
	}
	public PlayerLevelExtraTemplate getPlayerLevelTemplate(int lv) {
		int tempLv = Math.min(lv, getMaxLv());
		return this.levelTemplates[tempLv-1];
	}
	
	public int getPlayerLevel(int playerLv,long exp) {
		int maxLv = getMaxLv();
		if(playerLv >= maxLv) {
			return maxLv;
		}
		while(true) {
			if(exp >= getPlayerLevelTemplate(playerLv+1).getExp_totle()) {
				playerLv ++;
				if(playerLv >= maxLv) {
					break;
				}
			}else {
				break;
			}
		}
		return Math.min(maxLv, playerLv);
	}
	//根据征收的类型获取征收到的物品和类型
	public Items getLevy(BasePlayer player,int type){
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int rate = commValueConfig.getCommValue(CommonValueType.TreasuryRate);
		PlayerLevelTemplate template = getPlayerLevelTemplate(player);
		int cityCount = player.playerMission().getRelOpenCity();
		if(type==TreasuryCollectType.OilLevy.getType()) return new Items(PlayerAssetEnum.Oil.getTypeId(),template==null?0:template.getLevy_oil()+cityCount*rate,ItemType.CURRENCY.getType());
		if(type==TreasuryCollectType.CashLevy.getType()) return new Items(PlayerAssetEnum.Cash.getTypeId(),template==null?0:template.getLevy_cash()+cityCount*rate,ItemType.CURRENCY.getType());
		return new Items();
	}
	
	public int getExpeditionSweep(Player player) {
		int lv = player.playerLevel().getLv();
		int historyId = player.playerExpedition().getHistoryId();
		for (int i = lv; i > 0; i--) {
			PlayerLevelExtraTemplate template = getPlayerLevelTemplate(i);
			if(historyId >= template.getExpedition_clean()) {
				return template.getExpedition_clean();
			}
		}
		return 0;
	}

	/**
	 * 由总经验和等级 计算等级后剩余经验
	 * @param totalExp 总经验
	 * @param oldLv 当前等级
	 * @return 剩余经验
	 */
	public long calLvExp(long totalExp,int oldLv){
		long lvExp = totalExp - getLevelTotalExp(oldLv);
		if (lvExp <= 0L){
			return 1L;
		}
		return lvExp;
	}
	
	public PlayerLvCompensateTemplate getPlayerLvCompensateTemplate(int lv) {
		return this.lvCompensateMap.get(lv);
	}
}
  
