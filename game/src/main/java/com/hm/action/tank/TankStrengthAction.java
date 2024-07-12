package com.hm.action.tank;


import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.tank.biz.MagicReformBiz;
import com.hm.action.tank.biz.TankStrengthBiz;
import com.hm.config.BuildConfig;
import com.hm.config.GameConstants;
import com.hm.config.MagicReformConfig;
import com.hm.config.PlayerFunctionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.BuildingEnhanceAttriTemplate;
import com.hm.config.excel.templaextra.TankTrainingGroundTemplate;
import com.hm.enums.CDType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.cd.CdData;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.TrainTank;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankStrength;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * 坦克强化(基地内坦克养成)
 * @author xjt
 * @date 2020年2月24日16:58:39
 */
@Action
public class TankStrengthAction extends AbstractPlayerAction {
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MagicReformBiz magicReformBiz;
	@Resource
	private MagicReformConfig magicReformConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private PlayerFunctionConfig playerFunctionConfig;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private TankStrengthBiz tankStrengthBiz;
	@Resource
	private PlayerBiz playerBiz;
	/**
	 * 强化(喂鱼)
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_TankStrength)
	public void lvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		int lv = player.playerBuild().getBuildLv(GameConstants.TrainCentreId);
		//特训中心等级》=1级开启
		if(lv<=0) {
			return;
		}
		TankStrength tankStrength = tank.getTankStrength();
		int type = msg.getInt("type");
		int index = msg.getInt("index");
		
		
		int breachLv = tankStrength.getBreachLv(type);//根据培养等级获取该等级上的突破等级
		BuildingEnhanceAttriTemplate template = buildConfig.getEchance(type, index,breachLv);
		if(template==null) {
			return;
		}
		int num = tankStrength.getStrengthNum(template.getNeed_id());
		//根据强化等级和突破等级获取可培养的上限
		int needNumLimit=template.getNeed_num();
		//已经培养到当前突破等级的上限
		if(num>=needNumLimit) {
			return;
		}
		Items cost = buildConfig.getTankStrengthCost(template.getNeed_id());//获取强化所需的消耗
		
		
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Tank_Strength.value(tankId))) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		player.playerTank().strength(tankId,template.getNeed_id());
		player.notifyObservers(ObservableEnum.TankStrength, tankId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankStrength);
	}
	
	/**
	 * 突破
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_TankStrength_Breach)
	public void breach(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		int lv = player.playerBuild().getBuildLv(GameConstants.TrainCentreId);
		//特训中心等级》=1级开启
		if(lv<=0) {
			return;
		}
		int type = msg.getInt("type");
		//
		TankStrength tankStrength = tank.getTankStrength();
		int breachLv = tankStrength.getBreachLv(type);
		int breachLvMax = buildConfig.getBreakLvMax(type);
		//已经突破到最高等级
		if(breachLv>=breachLvMax) {
			return;
		}
		//判断是否满足突破条件
		if(!tankStrengthBiz.isCanBreach(tankStrength,type,breachLv)) {
			return;
		}
		List<Items> cost = buildConfig.getBreakCost(type, breachLv);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Tank_Strength.value(tankId))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		
		boolean flag = tankStrength.breach(type);
		player.notifyObservers(ObservableEnum.TankStrengthBreach, tankId,flag);
		player.playerTank().SetChanged();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankStrength_Breach,flag);
	}
	
	/**
	 * 坦克大战开始
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_TankFight_Start)
	public void startFight(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		int state = msg.getInt("state");
		TankTrainingGroundTemplate template = buildConfig.getTrain(id);
		if(template==null) {
			return;
		}
		int buildId = Integer.parseInt(template.getUnlock().split(":")[0]);
		int needLv = Integer.parseInt(template.getUnlock().split(":")[1]);
		int lv = player.playerBuild().getBuildLv(buildId);
		if(lv<needLv) {
			return;
		}
		CdData data = player.getPlayerCDs().getCdDataByCdType(CDType.TankTrain);
		if(data.getCount()<=0) {
			return;
		}
		List<Items> costs = Lists.newArrayList();
		costs.addAll(template.getTrainCosts());
		if(state>0) {
			costs.addAll(template.getBuffCosts());
		}
		if (!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.Advance)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		playerBiz.addAdvances(player, costs, LogType.TankTrain);
		//生成npc
		Map<Integer,TrainTank> map = tankStrengthBiz.createNpc(id,state,false);
		player.playerTrain().createNpc(id,state,map);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankFight_Start,map);
		
	}
	
	/**
	 * 坦克大战
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_TankFight)
	public void fight(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		int count = msg.getInt("count");//復活次數
		TankTrainingGroundTemplate template = buildConfig.getTrain(id);
		if(template==null) {
			return;
		}
		int buildId = Integer.parseInt(template.getUnlock().split(":")[0]);
		int needLv = Integer.parseInt(template.getUnlock().split(":")[1]);
		int lv = player.playerBuild().getBuildLv(buildId);
		if(lv<needLv) {
			return;
		}
		CdData data = player.getPlayerCDs().getCdDataByCdType(CDType.TankTrain);
		if(data.getCount()<=0) {
			return;
		}
		Items reviveCost = template.getReviveCost();
		if(count>0) {
			reviveCost.setCount(count*reviveCost.getCount());
			if(!itemBiz.checkItemEnoughAndSpend(player, reviveCost, LogType.TankTrain)) {
				return;
			}
		}
		playerBiz.clearAdvanceRes(player);
		String npc = msg.getString("npc");
		List<Items> rewards = tankStrengthBiz.getTrainReward(player,npc);
		player.getPlayerCDs().touchCdEvent(CDType.TankTrain);
		itemBiz.addItem(player, rewards, LogType.TankTrain.value(id));
		if(StringUtil.splitStr2IntegerList(npc, ",").size()>=player.playerTrain().getNpcNum()){
			//本关通关
			player.playerTrain().clearen(id);
		}
		player.playerTrain().clearTrain();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankFight,rewards);
	}
	
	/**
	 * 坦克大战扫荡
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_TankTrain_Sweep)
	public void sweep(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		int state = msg.getInt("state");
		TankTrainingGroundTemplate template = buildConfig.getTrain(id);
		if(template==null) {
			return;
		}
		int buildId = Integer.parseInt(template.getUnlock().split(":")[0]);
		int needLv = Integer.parseInt(template.getUnlock().split(":")[1]);
		int lv = player.playerBuild().getBuildLv(buildId);
		if(lv<needLv) {
			return;
		}
		CdData data = player.getPlayerCDs().getCdDataByCdType(CDType.TankTrain);
		if(data.getCount()<=0) {
			return;
		}
		if(!player.playerTrain().isCanSweep(id)){
			//没有通关不能扫荡
			return;
		}
		List<Items> costs = Lists.newArrayList();
		costs.addAll(template.getTrainCosts());
		if(state>0) {
			costs.addAll(template.getBuffCosts());
		}
		if(!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.TankTrain)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		Map<Integer,TrainTank> map = tankStrengthBiz.createNpc(id,state,true);
		List<Items> rewards = tankStrengthBiz.getTrainReward(player,map);
		player.getPlayerCDs().touchCdEvent(CDType.TankTrain);
		itemBiz.addItem(player, rewards, LogType.TankTrain.value(id));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankTrain_Sweep,rewards);
	}
}
