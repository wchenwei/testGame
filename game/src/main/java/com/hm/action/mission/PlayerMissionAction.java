package com.hm.action.mission;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.bag.BagBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.mission.biz.PlayerMissionBiz;
import com.hm.action.mission.vo.MissionFightTemp;
import com.hm.action.mission.vo.MissionResultVo;
import com.hm.action.player.PlayerBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.CityConfig;
import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.CityGuideTemplate;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.enums.BattleType;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Action
public class PlayerMissionAction extends AbstractPlayerAction{
	@Resource
	private PlayerMissionBiz playerMissionBiz;
	@Resource
	private MissionConfig missionConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private LogBiz logBiz;

	@MsgMethod(MessageComm.C2S_PlayerFB_StopOnhook)
	public void C2S_PlayerFB_StopOnhook(Player player,JsonMsg msg){
		player.playerMission().setDeathWave(-1);

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFB_StopOnhook);
		retMsg.addProperty("deathWave",player.playerMission().getDeathWave());
		player.sendMsg(retMsg);
	}

	/**
	 * 攻打关卡
	 */
	@MsgMethod(MessageComm.C2S_PlayerFb_Fight_Start)
	public void fbFightStart(Player player,JsonMsg msg){
		int missionId = msg.getInt("missionId");

		if(!player.playerMission().isCanFight(missionId)){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			//不能攻打
			return;
		}
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		player.playerTemp().setMissionFightTemp(new MissionFightTemp(player,template));

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFb_Fight_Start);
		retMsg.addProperty("missionId",missionId);
		player.sendMsg(retMsg);
	}

	//每一波的奖励结算
	@MsgMethod(MessageComm.C2S_PlayerFB_WaveEnd)
	public void C2S_PlayerFB_WaveEnd(Player player,JsonMsg msg){
		int missionId = msg.getInt("missionId");

		boolean isEndWave = msg.getBoolean("isEnd");
		MissionFightTemp missionFightTemp = player.playerTemp().getMissionFightTemp();
		if(missionFightTemp == null) {
			log.error(player.getId()+" missionFightTemp==null" + missionId);
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		if(missionFightTemp.getMissionId() != missionId) {
			log.error(player.getId()+" missionId not " + missionId+"->"+missionFightTemp.getMissionId());
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		//本次杀死的位置信息
		int waveId = msg.getInt("wave");
		List<Integer> killList = StringUtil.splitStr2IntegerList(msg.getString("killPos"),",");
		boolean isWin = !msg.hasKey("result") || msg.getInt("result") == 1;

		if(isWin) {//不是失败结束的
			player.notifyObservers(ObservableEnum.MissionWave);
		}

		List<Items> itemsList = missionFightTemp.checkMaveKillItems(waveId,killList);
		if(CollUtil.isNotEmpty(itemsList)) {
			itemBiz.addItem(player,itemsList,LogType.Mission);
			if(!isEndWave) {
				player.sendUserUpdateMsg();
			}
		}

		if(isEndWave) {
			return;//最后一波不发送次消息
		}
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFB_WaveEnd);
		retMsg.addProperty("missionId",missionId);
		retMsg.addProperty("wave",waveId);
//		retMsg.addProperty("itemsList",itemsList);
		player.sendMsg(retMsg);
	}

	/**
	 * 攻打关卡
	 */
	@MsgMethod(MessageComm.C2S_PlayerFb_Fight)
	public void fbFight(Player player,JsonMsg msg){
		int missionId = msg.getInt("missionId");
		MissionFightTemp missionFightTemp = player.playerTemp().getMissionFightTemp();
		if(missionFightTemp == null){//如果已经结算过
			log.error(player.getId()+"mission error"+missionId);
			return;
		}
        int result = msg.getInt("result");//0-失败 1-成功
		int resultType = msg.getInt("resultType");//失败类型 =1 强制结束 =2挂机结束
		boolean isWin = result == 1;

		List<Integer> tankIdList = missionFightTemp.getTankIdList();
        //玩家胜利校验战力
        if (isWin && !playerMissionBiz.checkCombat(player, tankIdList, missionId)) {
			isWin = false;
			log.error(player.getId()+"mission 战力校验失败:"+missionId);
        }
		//=====================计算最后一波的奖励=======================
		msg.addProperty("isEnd",true);
		C2S_PlayerFB_WaveEnd(player,msg);
		//=====================计算最后一波的奖励=======================

		if(resultType == 0 && !isWin) {//强制结束触发挂机
			player.playerMission().setDeathWave(msg.getInt("wave"));
		}

		player.playerTemp().setMissionFightTemp(null);
		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_PlayerFb_Fight);
		retMsg.addProperty("result",isWin?1:0);
		retMsg.addProperty("deathWave",player.playerMission().getDeathWave());
		logBiz.addPlayerBattleLog(player, BattleType.MainBattle.getType(), missionId, player.getCombat(),isWin?1:0);
		if (!isWin) {//战斗失败
            player.notifyObservers(ObservableEnum.MainFbFail, missionId, tankIdList);
			player.sendUserUpdateMsg();

			retMsg.addProperty("missionId",player.playerMission().getMissionId());
			player.sendMsg(retMsg);
			return;
        }
		//战斗胜利了
		MissionResultVo vo = playerMissionBiz.fight(player, missionId,tankIdList);
		if(missionConfig.getMission(player.playerMission().getMissionId()) == null) {
			player.playerMission().setDeathWave(msg.getInt("wave"));
		}
		player.sendUserUpdateMsg();
		//返回下一关的id
		retMsg.addProperty("missionId",player.playerMission().getMissionId());
		player.sendMsg(retMsg);

		if(CollUtil.isNotEmpty(vo.getRewards())) {
			JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_ShowItemReward);
			showMsg.addProperty("missionId",missionId);
			showMsg.addProperty("itemList", vo.getRewards());
			player.sendMsg(showMsg);
		}

	}
	
	/**
	 * 扫荡
	 */
	@MsgMethod(MessageComm.C2S_PlayerFb_Sweep)
	public void sweep(Player player,JsonMsg msg){
		int missionId = msg.getInt("missionId");
		int sweepCount = msg.getInt("sweepCount");
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		//是否能扫荡
		if(player.playerMission().getRelOpenCity() < template.getCity()) {
			player.sendErrorMsg(SysConstant.Mission_Sweep_Not);
			return;
		}
		Items cost = template.getCostItem();//每次扫荡的消耗
		cost.setCount(cost.getCount()*sweepCount);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Mission.value(missionId))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		player.notifyObservers(ObservableEnum.EliminateBandits, sweepCount);
		player.notifyObservers(ObservableEnum.ArsenalSweep, sweepCount);
		playerMissionBiz.sweep(player, missionId,sweepCount);
		player.sendMsg(MessageComm.S2C_PlayerFb_Sweep, sweepCount);
	}

	
	@MsgMethod(MessageComm.C2S_CityGuide_Reward)
	public void cityGuide_Reward(Player player,JsonMsg msg){
		int id = msg.getInt("id");
		//是否满足领取条件
		int openCityId = player.playerMission().getRelOpenCity();
		CityGuideTemplate template = cityConfig.getCityGuide(id);
		if(template==null){
			return;
		}
		if(template.getRewardCityId()>openCityId){
			//没有达到领取条件
			return;
		}
		if(player.playerMission().isReceiveGuide(id)){
			//已经领取过该奖励
			return;
		}
		List<Items> rewards = playerMissionBiz.receiveCityGuide(player,id);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_CityGuide_Reward,rewards);
	}
	
	@MsgMethod(MessageComm.C2S_Sync_GuideInfo)
	public void syncGuideInfo(Player player,JsonMsg msg){
		int cityId = msg.getInt("cityId");
		player.playerMission().syncGuideInfo(cityId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Sync_GuideInfo);
	}
}
