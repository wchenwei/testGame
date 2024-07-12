package com.hm.action.overallWar;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.flop.biz.FlopBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.overallWar.biz.OverallWarBiz;
import com.hm.action.overallWar.vo.OverallWarOpponentVo;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.OverallWarConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.OverallWarRewardTemplate;
import com.hm.db.WarResultUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.OverallWarTroopType;
import com.hm.enums.ServerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerFunction;
import com.hm.model.war.BattleRecord;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.TankArmy;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Action
public class OverallWarAction extends AbstractPlayerAction{
	@Resource
	private OverallWarBiz overallWarBiz;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private OverallWarConfig overallWarConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private FlopBiz flopBiz;
	@Resource
	private CommValueConfig commValueConfig;
	
	@MsgMethod(MessageComm.C2S_OverallWar_Open)
	public void open(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())||player.playerLevel().getLv()<50){
			return;
		}
		JsonMsg result = new JsonMsg(MessageComm.S2C_OverallWar_Open);
		boolean state = overallWarBiz.isRunning();
		result.addProperty("state", state);
		if(state){//开放状态需要返回给客户端对手信息
			OverallWarOpponentVo vo = overallWarBiz.createOpponentVo(player);
			result.addProperty("opponent", vo);
		}
		player.sendMsg(result);
	}
	
	//@MsgMethod(MessageComm.C2S_OverallWar_Troop_Change)
	public void troopChange(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())||player.playerLevel().getLv()<50){
			return;
		}
		if(!overallWarBiz.isRunning()){
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;
		}
		int type = msg.getInt("type");
		//位置：坦克id,位置：tankId
		ArrayList<TankArmy> armyList = troopBiz.createTankArmys(msg.getString("armys"),player,false);
		OverallWarTroopType troopType = OverallWarTroopType.getTroopType(type);
		if(armyList.size()!=troopType.getType()){
			return;
		}
		if(!overallWarBiz.checkTroopState(player, type, armyList)){
			//上阵坦克不存在或该坦克已在其它队伍中
			return;
		}
		player.playerOverallWar().changeTroop(type,armyList);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_OverallWar_Troop_Change,true);
	}
	
	@MsgMethod(MessageComm.C2S_OverallWar_Troop_Change)
	public void troopChangeAll(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())||player.playerLevel().getLv()<50){
			return;
		}
		if(!overallWarBiz.isRunning()){
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;
		}
		if(!overallWarBiz.checkTroopStateAll(player,msg)){
			player.sendErrorMsg(SysConstant.OverallWar_Troop_Error);
			return;
		}
		overallWarBiz.createToops(player, msg);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_OverallWar_Troop_Change,true);
	}
	
	@MsgMethod(MessageComm.C2S_OverallWar_Match)
	public void match(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())||player.playerLevel().getLv()<50){
			return;
		}
		if(player.playerOverallWar().getFightNum() >= commValueConfig.getCommValue(CommonValueType.OverallWarMaxNum)){
			player.sendErrorMsg(SysConstant.OverallWar_FightNum_Not);
			return;
		}
		if(!overallWarBiz.isRunning()){
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;
		}
		//还有对手不能再次匹配
		if(StrUtil.isNotBlank(player.playerOverallWar().getOpponentId())){
			return;
		}
		OverallWarOpponentVo vo = overallWarBiz.matchOpponentNew2(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_OverallWar_Match,vo);
	}
	
	@MsgMethod(MessageComm.C2S_OverallWar_Fight)
	public void fight(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())||player.playerLevel().getLv()<50){
			return;
		}
		//没有对手不能战斗
		if(StrUtil.isBlank(player.playerOverallWar().getOpponentId())){
			return;
		}
		if(!overallWarBiz.isRunning()){
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;
		}
		List<WarResult> warResult = overallWarBiz.fight(player);
		//最终结果
		boolean result = warResult.stream().filter(t ->t.isAtkWin()).count()>=3;
		//结算
		int scoreAdd = overallWarBiz.doResult(player,result);
		
		player.notifyObservers(ObservableEnum.OverallWar);
		player.sendUserUpdateMsg();
		JsonMsg returnMsg = new JsonMsg(MessageComm.S2C_OverallWar_Fight);
		returnMsg.addProperty("result", warResult);
		returnMsg.addProperty("score", scoreAdd);
		player.sendMsg(returnMsg);
	} 
	
	@MsgMethod(MessageComm.C2S_OverallWar_Reward)
	public void continueWinReward(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())||player.playerLevel().getLv()<50){
			return;
		}
		int id = msg.getInt("id");
		if(player.playerOverallWar().isReceive(id)){
			return;
		}
		//是否符合领奖条件
		if(!overallWarBiz.isCanReceive(player,id)){
			return;
		}
		OverallWarRewardTemplate template = overallWarConfig.getReward(id);
		if(template==null){
			return;
		}
		player.playerOverallWar().receive(id);
		List<Items> reward = template.getRewards();
		itemBiz.addItem(player, reward, LogType.OverallWar);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_OverallWar_Reward, reward);
		//翻牌
		List<Items> flop = flopBiz.createFlop(player, template.getFlops());
		if(!flop.isEmpty()){
			player.sendMsg(MessageComm.S2C_Flop_Start, flop);
		}
	}
	
	@MsgMethod(MessageComm.C2S_OverallWar_Record)
	public void record(Player player, JsonMsg msg){
		List<BattleRecord> battRecordList = player.playerOverallWar().getRecordList()
				.stream().map(e -> WarResultUtils.getBattleRecord(player.getServerId(), e))
				.filter(Objects::nonNull).collect(Collectors.toList()); 
		player.sendMsg(MessageComm.S2C_OverallWar_Record,battRecordList);
	}
}
