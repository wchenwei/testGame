package com.hm.log;

import cn.hutool.core.util.StrUtil;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Aircraft;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.PlayerAircraftCarrier;
import com.hm.model.tank.ElementPos;
import com.hm.model.tank.ElementSkill;
import com.hm.model.tank.Tank;
import com.hm.redis.PlayerRedisData;
import com.hm.rmi.LogServerRmiHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class LogBiz {

	@Resource
	private LogServerRmiHandler logServerRmiHandler;

	// 记录玩家资产消耗日志
	public void currencySpendLog(BasePlayer player, CurrencyKind currencyKind, long spend, LogType logType) {
        if (logType == null || logType == LogType.Advance) return;
		try {
			logServerRmiHandler.addItemOutLog(player,
					currencyKind.getIndex(),
					ItemType.CURRENCY.getType(), spend, logType);
		} catch (Exception e) {
			log.error("玩家"+player.getId()+"消耗"+currencyKind.toString()+"出错");
		}
	}
	public void currencyAddLog(BasePlayer player, CurrencyKind currencyKind, long add, LogType logType) {
		if(logType == null) return;
		try {
			logServerRmiHandler.addItemInLog(player,
					currencyKind.getIndex(),
					ItemType.CURRENCY.getType(), 
					add, 
					logType);
		} catch (Exception e) {
			log.error("玩家"+player.getId()+"添加"+currencyKind.toString()+"出错",e);
		}
	}
	//增加vip点数日志
	public void vipExpIn(BasePlayer player, int num, LogType logType){
		logServerRmiHandler.addItemInLog(player, PlayerAssetEnum.VIPDOTCOUNT.getTypeId(), ItemType.CURRENCY.getType(), num, logType);
	}
	//增加经验日志
	public void playerExpIn(BasePlayer player, long num, LogType logType){
		logServerRmiHandler.addItemInLog(player, PlayerAssetEnum.EXP.getTypeId(), ItemType.CURRENCY.getType(), num, logType);
	}
	public void spendGoods(BasePlayer player, int itemId, long count, int itemType, LogType logType) {
		logServerRmiHandler.addItemOutLog(player, itemId, itemType, count, logType);
	}
	public void addGoods(BasePlayer player, int itemId, long count, int itemType, LogType logType) {
		logServerRmiHandler.addItemInLog(player, itemId, itemType, count, logType);
	}
	
	public void savePlayerRegisterLog(BasePlayer player) {
		logServerRmiHandler.addPlayerLoginLog(player, 1);
	}
	public void savePlayerLoginLog(BasePlayer player) {
		logServerRmiHandler.addPlayerLoginLog(player, 0);
	}
	public void addRechargeLog(BasePlayer player,int rechargeId,long rmb) {
		logServerRmiHandler.addRechargeLog(player, rechargeId, rmb);
	}
	public void addDelMailLog(BasePlayer player,List<String> ids) {
		logServerRmiHandler.addDelMailLog(player, ids);
	}

	public void addTaskAddLog(BasePlayer player, GameTaskType taskType, int taskId){
		logServerRmiHandler.addTaskLog(player, taskType.getType(), 0, taskId);
	}

	public void addTaskCompleteLog(BasePlayer player, GameTaskType taskType, int taskId){
		logServerRmiHandler.addTaskLog(player, taskType.getType(), 1, taskId);
	}

	public void addTaskCompleteLog(BasePlayer player, GameTaskType taskType, int taskId, long timeUsedSecond){
		logServerRmiHandler.addTaskLog(player, taskType.getType(), 1, taskId, timeUsedSecond);
	}

	public void addPlayerLevelLog(BasePlayer player) {
		logServerRmiHandler.addPlayerLevel(player);
	}
	public void addPlayerBattleLog(BasePlayer player,int battleType,int missionId,long combat,int result){
		logServerRmiHandler.addPlayerBattleLog(player,battleType,missionId,combat,result);
	}
	public void saveFBLog(BasePlayer player,int missionId,boolean win,int star, boolean isSweep) {
		logServerRmiHandler.addPVELog(player, missionId, win, star, isSweep);
	}
	public void addPlayerActionLog(BasePlayer player,int type,String extra) {
		logServerRmiHandler.addPlayerActionLog(player, type, extra);
	}

    public void addPlayerPersonChatLog(BasePlayer player, long type, String extra) {
        logServerRmiHandler.addPlayerPersonChatLog(player, type, extra);
    }

	public void addPlayerActionLog(BasePlayer player, ActionType type, String extra) {
		addPlayerActionLog(player, type.getCode(), extra);
	}

	public void addPlayerActionLog(PlayerRedisData player, ActionType type, String extra) {
		logServerRmiHandler.addPlayerActionLog(player, type, extra);
	}

	public void addPlayerFirstLoginLog(BasePlayer player) {
		try{
			List<Tank> tankList = player.playerTank().getTankList();
			String tankListData =tankList.stream().sorted(Comparator.comparing(Tank:: getCombat).reversed())
					.limit(10).map(e->{
						return getTankControlStr(e);
					}).collect(Collectors.joining(";"));

			logServerRmiHandler.addPlayerActionLog(player, ActionType.TankControlMsg.getCode(), tankListData);
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			logServerRmiHandler.addPlayerActionLog(player, ActionType.AircraftCarrier.getCode(), getAircraftCarrierStr(player));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void addPlayerTroopLog(BasePlayer player){
		logServerRmiHandler.addPlayerTroopLog(player);
	}

	public void addPlayerEquipLog(BasePlayer player, int type, double [] lvs){
		logServerRmiHandler.addPlayerEquipLog(player, type, lvs);
	}


	private String getTankControlStr(Tank e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.getId());
		sb.append("_pos:");
		ElementPos[] elemtPost = e.getControl().getPos();
		for(int i=0; i<elemtPost.length; i++) {
			sb.append(String.format("%s_%s_%s#", i, elemtPost[i].getElementId(),elemtPost[i].getColor()));
		}
		sb.append("&kill:");
		Arrays.stream(e.getControl().getSkills()).forEach(g->{
			if(null==g) {
				return;
			}
			sb.append(String.format("%s_%s#", g.getSkillId(),g.getLv()));
		});

		sb.append("&bigskill:");
		ElementSkill bigSkill = e.getControl().getBigSkill();
		if(null!=bigSkill) {
			sb.append(String.format("%s_%s#", bigSkill.getSkillId(),bigSkill.getLv()));
		}
		Map<Integer, Integer> advance =  e.getDriver().getAdvance();
		sb.append("&driveradvance:");
		advance.forEach((key, value)->{
			sb.append(String.format("%s_%s#", key, value));
		});
		Map<Integer, Integer> teachData = e.getTankTech().getTechData();
		sb.append("&teach:");
		teachData.forEach((key, value)->{
			sb.append(String.format("%s_%s#", key, value));
		});
		return sb.toString();
	}

	private String getAircraftCarrierStr(BasePlayer player) {
		StringBuilder sb = new StringBuilder();
		PlayerAircraftCarrier aircriftCarrier = player.playerAircraftCarrier();
		sb.append(String.format("lv:%s;enginelv:%s;land:", aircriftCarrier.getLv(), aircriftCarrier.getEngineLv()));
		aircriftCarrier.getIsland().forEach((key, value)->{
			sb.append(String.format("%s_%s#", key, value));
		});
		String[] aircrafts = aircriftCarrier.getAircrafts();
		StringBuilder sbAircrafts = new StringBuilder();
		for(int i=0; i<aircrafts.length; i++) {
			if(StrUtil.isNotEmpty(aircrafts[i])) {
				Aircraft tempAircraft = player.playerAircraft().getAircraft(aircrafts[i]);
				sbAircrafts.append(String.format("%s_%s_%s#", i+1, tempAircraft.getId(), tempAircraft.getStar()));
			}
		}
		sb.append(String.format(";aircrafts:%s", sbAircrafts.toString()));
		return sb.toString();
	}
}
