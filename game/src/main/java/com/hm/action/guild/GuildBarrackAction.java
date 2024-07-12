package com.hm.action.guild;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.bag.BagBiz;
import com.hm.action.guild.biz.GuildBarrackBiz;
import com.hm.action.guild.vo.GuildTroopVo;
import com.hm.action.player.PlayerBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.biz.WorldTroopBiz;
import com.hm.config.excel.GuildConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.enums.LogType;
import com.hm.enums.TroopPosition;
import com.hm.enums.TroopState;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.SysConstant;
import com.hm.util.MathUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.troop.TankArmy;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Action
public class GuildBarrackAction extends AbstractGuildAction{
	@Resource
	private GuildBarrackBiz guildBarrackBiz;
	@Resource
	private WorldTroopBiz worldTroopBiz;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BagBiz bagBiz;
	@Resource
	private GuildConfig guildConfig;
	//请求驻地信息
	@MsgMethod(MessageComm.C2S_GuildBarrack_Open)
	public void open(Player player, Guild guild, JsonMsg msg){
		List<GuildTroopVo> vos = guildBarrackBiz.createTroopVo(guild);
		player.sendMsg(MessageComm.S2C_GuildBarrack_Open, vos);
		
	}
	//派军
	@MsgMethod(MessageComm.C2S_GuildBarrack_Dispatch)
	public void dispatch(Player player, Guild guild, JsonMsg msg){
		String ids = msg.getString("troopIds");
		List<String> troopIds = StringUtil.splitStr2StrList(ids, ",");
		//根据部落等级获取驻守最大数量
		int maxNum = guildConfig.getGuildTroopLimit(guild.guildLevelInfo().getLv());
		if(guild.getGuildBarrack().getTroopNum()+troopIds.size()>maxNum){
			player.sendErrorMsg(SysConstant.Guild_Barrack_Troop_Limit);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Dispatch, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		if(!guildBarrackBiz.isCanDispatch(player,troopIds)){
			player.sendErrorMsg(SysConstant.Guild_Barrack_Troop_Dispatch);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Dispatch, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		guildBarrackBiz.dispatch(player, troopIds);
		player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
	}
	//撤退
	@MsgMethod(MessageComm.C2S_GuildBarrack_Retreat)
	public void retreat(Player player, Guild guild, JsonMsg msg){
		String troopId = msg.getString("troopId");
		
		if(!player.playerTroops().isContain(troopId)) {
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
		if(worldTroop == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		if(worldTroop.getTroopPosition()!=TroopPosition.GuildBarrack.getType()){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		try {
			worldTroop.lockTroop();
			guildBarrackBiz.retreatOrRepatriate(guild, worldTroop);
		} finally {
			worldTroop.unlockTroop();
		}
		player.sendMsg(MessageComm.S2C_GuildBarrack_Retreat);
		player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
		
	}
	//遣返
	@MsgMethod(MessageComm.C2S_GuildBarrack_Repatriate)
	public void repatriate(Player player, Guild guild, JsonMsg msg){
		//该部队不是自己的部队
		String troopId = msg.getString("troopId");

		if(!guild.isManamger(player)){
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player.getServerId()).getWorldTroop(troopId);
		if(worldTroop == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		if(worldTroop.getTroopPosition()!=TroopPosition.GuildBarrack.getType()){
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		try {
			worldTroop.lockTroop();
			guildBarrackBiz.retreatOrRepatriate(guild, worldTroop);
		} finally {
			worldTroop.unlockTroop();
			player.sendMsg(MessageComm.S2C_GuildBarrack_Repatriate, true);
		}
		player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
	}
	
	//修理
	@MsgMethod(MessageComm.C2S_GuildBarrack_Repair)
	public void repair(Player player, Guild guild, JsonMsg msg){
		String troopId = msg.getString("troopId");
		if(!guild.isManamger(player)){
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		WorldTroop worldTroop = TroopServerContainer.of(player.getServerId()).getWorldTroop(troopId);
		if(worldTroop == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		//死亡或者可回血状态
		List<TankArmy> recoverList = worldTroop.getTroopArmy().getTankList().stream()
								.filter(e -> !e.isFullHp()).collect(Collectors.toList());
		boolean isCanRecoverHp = worldTroop.getState() == TroopState.Death.getType()
				|| worldTroop.getState() == TroopState.None.getType()
				 && CollUtil.isNotEmpty(recoverList);
		if(!isCanRecoverHp) {
			player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
			return;
		}
		long spendOil = troopBiz.getRecoveryCost(player,recoverList);
		//检查消耗
		if(!playerBiz.checkPlayerCurrency(player, CurrencyKind.Oil, spendOil)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		int oldState = worldTroop.getState();
		//需要总秒数
		long totalSecond = troopBiz.calTroopRecoveTime(player,worldTroop,recoverList);
		//当前背包内的锤子数量
		long itemNum = player.playerBag().getItemCount(ItemConstant.TroopRepairHammer);
		//每个锤子抵消的秒数
		int oneItemSecond = itemConfig.getItemTemplateById(ItemConstant.TroopRepairHammer).getValue();
		//实际消耗道具数量
		long spendItemNum = Math.min(itemNum, troopBiz.getTroopFullHpNeedItemNum(totalSecond, oneItemSecond));
		//需要用金币抵消的秒数
		long lastGoldSecond = Math.max(totalSecond-spendItemNum*oneItemSecond, 0);
		long spendGold = (long)Math.ceil(2*MathUtils.div(lastGoldSecond, 60));
		//扣除满血所需金币
		if(spendGold > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, spendGold, LogType.HandTroopFullHp)) {
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		if(spendItemNum > 0) {
			bagBiz.spendItem(player, ItemConstant.TroopRepairHammer, spendItemNum, LogType.HandTroopFullHp);
		}
		playerBiz.spendPlayerCurrency(player, CurrencyKind.Oil, spendOil, LogType.WorldTroopRecoverHp);
		try {
			worldTroop.lockTroop();
			worldTroop.getTroopArmy().getTankList().forEach(e -> e.setFullHp());
			worldTroop.getTroopArmy().SetChanged();
			worldTroop.changeState(TroopState.None);//改为空闲状态
			if(oldState == TroopState.Death.getType()) {
				//把部队放到该放的地方
				troopBiz.addWorldTroopToWorldAndUpdate(player, worldTroop);
			}else{
				worldTroop.saveDB();
				troopBiz.sendWorldTroopUpdate(player, worldTroop);
			}
		} finally {
			worldTroop.unlockTroop();
		}
		player.sendUserUpdateMsg();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GuildBarrack_Repair);
		serverMsg.addProperty("spendOil", spendOil);
		serverMsg.addProperty("spendGold", spendGold);
		serverMsg.addProperty("spendItemNum", spendItemNum);
		player.sendMsg(serverMsg);
		player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
	}
	
	//出征
	@MsgMethod(MessageComm.C2S_GuildBarrack_Expedition)
	public void expedition(Player player, Guild guild, JsonMsg msg){
		//部队id
		String troopIds = msg.getString("troopIds");
		int cityId = msg.getInt("cityId");
		if(!guild.isManamger(player)){
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		int troopNum = guildBarrackBiz.expedition(guild,cityId,StringUtil.splitStr2StrList(troopIds, ","));
		player.sendMsg(MessageComm.S2C_GuildBarrack_Expedition, troopNum);
		player.sendMsg(MessageComm.S2C_GuildBarrack_Open, guildBarrackBiz.createTroopVo(guild));
	}

}
