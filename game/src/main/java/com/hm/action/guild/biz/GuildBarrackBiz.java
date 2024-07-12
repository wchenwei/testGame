package com.hm.action.guild.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.guild.vo.GuildTroopVo;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.biz.WorldTroopBiz;
import com.hm.config.CityConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.TroopPosition;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.troop.TroopItemContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
@Slf4j
@Biz
public class GuildBarrackBiz {
	

	@Resource
	private TroopBiz troopBiz;
	@Resource
	private CityConfig cityConfig;

	@Resource
	private WorldBiz worldBiz;
	@Resource
	private WorldTroopBiz worldTroopBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private CommValueConfig commValueConfig;
	

	public boolean isCanDispatch(Player player,List<String> troopIds){
		for(String troopId:troopIds){
			if(!player.playerTroops().isContain(troopId)){
				return false;
			}
			if(!worldTroopBiz.isCanOperationTroop(player.getServerId(),troopId)){
				return false;
			}
			WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
			if(worldTroop==null||!worldTroopBiz.isCanOperationTroop(worldTroop)||worldTroop.getWorldType()!=WorldType.Normal.getType()){
				return false;
			}
		}
		return true;
	}
	public void dispatch(Player player,WorldTroop worldTroop){
		Guild guild = guildBiz.getGuild(player);
		guild.getGuildBarrack().addTroops(worldTroop.getId());
		worldTroop.changePosition(TroopPosition.GuildBarrack);
		worldTroop.saveDB();
		guild.saveDB();
		troopBiz.sendWorldTroopUpdate(player, worldTroop);
	}
	public void dispatch(Player player, List<String> troopIds) {
		TroopItemContainer troopContainer = TroopServerContainer.of(player.getServerId());
		troopIds.forEach(troopId ->{
			WorldTroop worldTroop = troopContainer.getWorldTroop(troopId);
			dispatch(player, worldTroop);
		});
	}
	
	//撤退或遣返
	public void retreatOrRepatriate(Guild guild,WorldTroop worldTroop){
		guild.getGuildBarrack().retreat(worldTroop.getId());
		try{
			worldTroop.lockTroop();
			worldTroop.changePosition(TroopPosition.None);
			worldTroop.saveDB();
		}finally{
			worldTroop.unlockTroop();
		}
		//给部队所属玩家发送部队变化消息,只需要向在线的发送
		Player player = PlayerUtils.getOnlinePlayer(worldTroop.getPlayerId());
		if(player!=null){
			//发送部队变化
			troopBiz.sendWorldTroopUpdate(player, worldTroop);
		}
		guild.saveDB();
	}

	public List<GuildTroopVo> createTroopVo(Guild guild) {
		TroopItemContainer troopContainer = TroopServerContainer.of(guild.getServerId());
		List<GuildTroopVo> vos = Lists.newArrayList();
		guild.getGuildBarrack().getTroops().forEach(troopId ->{
			WorldTroop worldTroop = troopContainer.getWorldTroop(troopId);
			if(worldTroop!=null){
				Player player = PlayerUtils.getPlayer(worldTroop.getPlayerId());
				Optional<Player> optional = Optional.ofNullable(player);
				optional.ifPresent(t ->{
					vos.add(new GuildTroopVo(t,worldTroop));
				});
			}
		});
		return vos;
	}
	public int expedition(Guild guild,int cityId, List<String> troopIds) {
		int troopNum = 0;
		TroopItemContainer troopContainer = TroopServerContainer.of(guild.getServerId());
		for(String troopId:troopIds){
			WorldTroop worldTroop = troopContainer.getWorldTroop(troopId);
			if(worldTroop==null){
				continue;
			}
			//在部落大营而且状态处于空闲
			if(worldTroop.getTroopPosition()==TroopPosition.GuildBarrack.getType()&&worldTroop.getState()==TroopState.None.getType()){
				Player player = PlayerUtils.getPlayer(worldTroop.getPlayerId());
				if(player==null){
					continue;
				}
				List<Integer> ways = cityConfig.getShotWayGuildBarrack(player,worldTroop.getCityId(), cityId);
				if(ways.isEmpty()){
					continue;
				}
				WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(worldTroop.getCityId());
				System.err.println("部落派兵选择路线:"+worldTroop.getPlayerId()+":"+ways);
				//检查是否能把通过这些点
				if(troopBiz.canAdoptWays(player,worldTroop,ways)){
					guild.getGuildBarrack().transfer(worldTroop.getPlayerId());
					if(!worldCity.removeTroopAndSave(troopId)) {
						log.error(troopId+"删除失败:在"+worldCity.getId()+" 删除失败!");
					}
					troopBiz.stratMove(worldTroop, ways);
					troopBiz.sendWorldTroopUpdate(player, worldTroop);
					//广播城市部队变化
					worldBiz.broadWorldCityTroopChange(worldCity);
					troopNum++;
				}
			}
		}
		return troopNum;
	}
	//离开部落大营
	public void leaveGuildBarrack(Guild guild, Player player) {
		TroopItemContainer troopItemContainer = TroopServerContainer.of(player);
		if(guild.getGuildBarrack().getTransferIds().contains(player.getId())) {
			guild.getGuildBarrack().delTreansferId(player.getId());
		}
		//将玩家的部队位置修改为默认值
		player.playerTroops().getTroopIdList().forEach(troopId ->{
			WorldTroop worldTroop = troopItemContainer.getWorldTroop(troopId);
			if(worldTroop!=null&&worldTroop.getTroopPosition()==TroopPosition.GuildBarrack.getType()){
				retreatOrRepatriate(guild, worldTroop);
			}
		});
	}

}
