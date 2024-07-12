package com.hm.action.drop;

import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.captive.CaptiveBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.temlate.GuildTecTemplateImpl;
import com.hm.config.excel.templaextra.WarModeDropTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.PlayerTroop;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 玩家掉落监听
 * @author siyunlong
 * @date 2018年12月14日 上午11:33:57
 * @version V1.0
 */
@Biz
public class PlayerDropObserver{
	@Resource
	private DropConfig dropConfig;
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	@Resource
	private ActivityBiz activityBiz;
	@Resource
    private CaptiveBiz captiveBiz;
	@Resource
	private GuildTechBiz guildTechBiz;
	/**
	 * 计算玩家掉落
	 * @param warResult
	 */
	public void calWarResultPlayerWinDrop(WorldCity worldCity,WarResult warResult) {
		UnitGroup unitGroup = warResult.getWinUnitGroup();
		AbstractFightTroop winTroop = warResult.getWinUnitGroup().getFightTroop();
		if(winTroop instanceof PlayerTroop) {
			PlayerTroop playerTroop = (PlayerTroop)winTroop;
			Player player = PlayerUtils.getPlayer(playerTroop.getPlayerId());
			if(player != null && !player.isKFPlayer()) {
				AbstractFightTroop loseTroop = warResult.getLoseUnitGroup().getFightTroop();
				unitGroup.setPlayerDropList(calPlayerWarDrop(player, worldCity, winTroop, loseTroop));
				//判断部队俘虏
				captiveBiz.doPlayerTankCaptive(player, warResult);
			}
		}
	}

	public List<Items> calPlayerWarDrop(Player player, WorldCity worldCity, AbstractFightTroop winTroop, AbstractFightTroop loseTroop) {
    	//战斗常规掉落
    	List<Items> itemList = dropConfig.checkWarPlayerDrops(player);
    	itemList.addAll(dropConfig.checkPlayerDrops(player, buildActivityDropList(player,worldCity,loseTroop)));
		//计算部落科技额外掉落功勋
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if(guild != null) {
			GuildTecTemplateImpl template = guildTechBiz.getGuildTecTemplate(guild, GuildTecFunEnum.RateCron);
			int cronVal = template.getContrVal(guild);
			if(cronVal > 0) {
				itemList.add(PlayerAssetEnum.Credit.buildItems(cronVal));
			}
		}
    	return itemList;
    }


    
    public List<WarModeDropTemplate> buildActivityDropList(Player player,WorldCity worldCity,AbstractFightTroop loseTroop) {
    	List<WarModeDropTemplate> templateList = Lists.newArrayList();
    	templateList.addAll(checkActivityDropList(player,DropModeType.ActivityMode));
    	//增加军工厂掉落模块
    	templateList.addAll(checkGuildFactoryDrop(player,DropModeType.GuildFactoryDropMode));
    	return templateList;
    }
    
    public List<WarModeDropTemplate> checkActivityDropList(Player player,DropModeType dropModeType) {
    	List<WarModeDropTemplate> resultList = Lists.newArrayList();
    	for (WarModeDropTemplate warModeDropTemplate : dropConfig.getActivityWarModeDropList(dropModeType)) {
    		ActivityType activityType = warModeDropTemplate.getActivityType();
    		if(activityType != null) {
    			AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
    			if(activity != null && activity.isOpen()&&!activity.isCloseForPlayer(player)
    					&& warModeDropTemplate.isFitActivity(activityType, activityBiz.getActivityVersion(activity))) {
    				resultList.add(warModeDropTemplate);
    	    	}
    		}
		}
    	return resultList;
    }
    
    public List<WarModeDropTemplate> checkGuildFactoryDrop(Player player,DropModeType dropModeType) {
    	List<WarModeDropTemplate> resultList = Lists.newArrayList();
    	if(player.playerArms().getState()!=1){
    		return resultList;
    	}
    	for (WarModeDropTemplate warModeDropTemplate : dropConfig.getActivityWarModeDropList(dropModeType)) {
    		resultList.add(warModeDropTemplate);
		}
    	return resultList;
    }
}
