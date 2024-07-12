package com.hm.action.levelEvent;

import com.google.common.collect.Lists;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.levelEvent.biz.LevelEventBiz;
import com.hm.action.mission.vo.MissionResultVo;
import com.hm.action.player.PlayerBiz;
import com.hm.config.LevelEventConfig;
import com.hm.config.excel.temlate.MissionEventBuffTemplate;
import com.hm.config.excel.temlate.MissionEventWarTemplate;
import com.hm.config.excel.templaextra.LevelEventNewTemplate;
import com.hm.config.excel.templaextra.LevelEventRewardNewTemplate;
import com.hm.config.excel.templaextra.LevelEventRewardTemplate;
import com.hm.config.excel.templaextra.LevelEventTemplate;
import com.hm.enums.EventBuffType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerRewardMode;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.message.MessageComm;
import com.hm.model.battle.LastFightResultVo;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.LevelEvent;
import com.hm.model.player.LevelEventNew;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.List;
@Action
public class LevelEventAction extends AbstractPlayerAction{
	@Resource
	private LevelEventBiz levelEventBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private LevelEventConfig levelEventConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	/**
	 * 战斗
	 */
	@MsgMethod(MessageComm.C2S_LevelEvent_Fight_Start)
    public void fightStart(Player player, JsonMsg msg) {
		int id = msg.getInt("id");//代表哪一关
		String tankIds = msg.getString("tankIds");
		LevelEventTemplate template = levelEventConfig.getLevelEvent(id);
		if(template==null){
			return ;
		}
		LevelEvent levelEvent = player.playerLevelEvent().getLevelEvent(template.getWar_id());
		if(levelEvent==null){//没有这个事件
			return ;
		}
		long spendOil = player.playerTank().getTankTotalOil(StringUtil.splitStr2IntegerList(tankIds, ","));
		if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Oil, spendOil, LogType.LevelEvent.value(id))){
			return;
		}
    	player.sendUserUpdateMsg();
	}
	/**
	 * 战斗
	 */
	@MsgMethod(MessageComm.C2S_LevelEvent_Fight)
    public void fight(Player player, JsonMsg msg) {
		int id = msg.getInt("id");//代表哪一关
		int star = msg.getInt("star");
		if(player.playerFight().isCalResult()){//如果已经结算过
			//把结果再发给客户端进行展示
			player.sendMsg(MessageComm.S2C_LevelEvent_Fight, player.playerFight().getResult());
			return;
		}
		//id:hp:mp,id:hp:mp......
		String result = msg.getString("result");
		LevelEventTemplate template = levelEventConfig.getLevelEvent(id);
		if(template==null){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		LevelEvent levelEvent = player.playerLevelEvent().getLevelEvent(template.getWar_id());
		if(levelEvent==null){//没有这个事件
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		if(!levelEvent.isCanFight(id)){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		if(!levelEventBiz.checkResult(player, id, result)){
			player.sendErrorMsg(SysConstant.TankResult_Not);
			return ;
		}
		//发放奖励
    	MissionResultVo resultVo = new MissionResultVo(player);
    	//处理结果，同步血量
    	levelEventBiz.doResult(player,template.getWar_id(), id, star, result);
    	if(star<=0){//失败不用发送结算信息
    		return;
    	}
    	List<Items> rewards = Lists.newArrayList();
    	if(!player.playerLevelEvent().isFighted(id)){
	    	rewards = template.getRewards();
	    	//计算等级加成活动
	    	rewards = activityEffectBiz.calActivityEffectItemsAdd(player, rewards, PlayerRewardMode.PlayerLvEvent);
    	}
    	resultVo.setRewards(rewards);
		player.playerLevelEvent().clearance(template.getWar_id(), id, star, result);
		itemBiz.addItem(player, rewards, LogType.LevelEvent.value(id));
		player.playerFight().doLastResult(new LastFightResultVo(player.playerFight().getFightId(),resultVo));
		player.sendMsg(MessageComm.S2C_LevelEvent_Fight,resultVo);
    	player.sendUserUpdateMsg();
	}
	/**
	 * 重置事件
	 */
	@MsgMethod(MessageComm.C2S_LevelEvent_Reset)
    public void reset(Player player, JsonMsg msg) {
		int warId = msg.getInt("warId");//代表哪一个战役
		LevelEvent levelEvent = player.playerLevelEvent().getLevelEvent(warId);
		if(levelEvent==null){//没有这个事件
			return ;
		}
		player.playerLevelEvent().reset(warId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_LevelEvent_Reset, warId);
		
	}
	/**
	 * 领取事件奖励
	 */
	@MsgMethod(MessageComm.C2S_LevelEvent_ReceiveReward)
	public void receiveReward(Player player, JsonMsg msg){
		int id = msg.getInt("id");
		if(!player.playerLevelEvent().canReceive(id)){
			return;
		}
		LevelEventRewardTemplate template = levelEventConfig.getReward(id);
		List<Items> rewards = template.getRewards();
		//计算等级加成活动
    	rewards = activityEffectBiz.calActivityEffectItemsAdd(player, rewards, PlayerRewardMode.PlayerLvEvent);
		
		itemBiz.addItem(player, rewards, LogType.LevelEventReward.value(id));
		player.playerLevelEvent().receive(id);
		player.playerLevelEvent().checkEvent();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_LevelEvent_ReceiveReward,rewards);
	}
	
	
	/**
	 * 战斗
	 */
	@MsgMethod(MessageComm.C2S_Event_Fight_Start)
    public void fightStartNew(Player player, JsonMsg msg) {
		int id = msg.getInt("id");//代表哪一关
		String tankIds = msg.getString("tankIds");
		LevelEventTemplate template = levelEventConfig.getLevelEvent(id);
		if(template==null){
			return ;
		}
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(template.getWar_id());
		if(levelEvent==null){//没有这个事件
			return ;
		}
		if(!levelEvent.isCanFight(id)){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		List<Integer> ids = StringUtil.splitStr2IntegerList(tankIds, ",");
		if(ids.size()>levelEventBiz.getOnTankNum(levelEvent.getBuffId())) {
			player.sendErrorMsg(SysConstant.TankType_Not);
			return;
		}
		long spendOil = player.playerTank().getTankTotalOil(ids);
		if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Oil, spendOil, LogType.LevelEvent.value(id))){
			return;
		}
    	player.sendUserUpdateMsg();
	}
	/**
	 * 战斗
	 */
	@MsgMethod(MessageComm.C2S_Event_Fight)
    public void fightNew(Player player, JsonMsg msg) {
		int id = msg.getInt("id");//代表哪一关
		int star = msg.getInt("star");
		if(player.playerFight().isCalResult()){//如果已经结算过
			//把结果再发给客户端进行展示
			player.sendMsg(MessageComm.S2C_LevelEvent_Fight, player.playerFight().getResult());
			return;
		}
		//id:hp:mp,id:hp:mp......
		String result = msg.getString("result");
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
		if(template==null){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(template.getWar_id());
		if(levelEvent==null){//没有这个事件
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		if(!levelEvent.isCanFight(id)){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		List<Integer> tankIds = levelEventBiz.getTankIds(result);
		//检查buff对出站坦克的限制
		if(tankIds.size()>levelEventBiz.getOnTankNum(levelEvent.getBuffId())) {
			player.sendErrorMsg(SysConstant.TankType_Not);
			return;
		}
		
		if(!levelEventBiz.checkResult(player, id, result)){
			player.sendErrorMsg(SysConstant.TankResult_Not);
			return ;
		}
		//发放奖励
    	MissionResultVo resultVo = new MissionResultVo(player);
    	//处理结果，同步血量
    	levelEventBiz.doResult(player,template.getWar_id(), id, star, result);
    	if(star<=0){//失败不用发送结算信息
    		return;
    	}
    	List<Items> rewards = Lists.newArrayList();
		if (levelEvent.getBuffId() > 0) {
			MissionEventBuffTemplate buffTemplate = levelEventConfig.getBuff(levelEvent.getBuffId());
			if (buffTemplate != null && buffTemplate.getType() == EventBuffType.Reward.getType()) {
				rewards = template.getRewards();
			}
		}
//    	if(!player.playerEvent().isFighted(id)){
//	    	rewards = template.getRewards();
//	    	//计算等级加成活动
//	    	rewards = activityEffectBiz.calActivityEffectItemsAdd(player, rewards, PlayerRewardMode.PlayerLvEvent);
//    	}
    	resultVo.setRewards(rewards);
    	//处理buff
		//levelEventBiz.doBuffEffect(player,levelEvent,tankIds);
		int starId = levelEvent.getNowId();
		player.playerEvent().clearance(template.getWar_id(), id, star, result);
		//清理buff
		levelEvent.clearBuff();
		//到达该点
		levelEventBiz.dispatch(player, levelEvent, id, false);
		if (starId != id) {
			levelEvent.addSteps();
		}
		//检查事件
		levelEventBiz.checkPointEvent(player, levelEvent, id);
		itemBiz.addItem(player, rewards, LogType.LevelEventNew.value(id));
		player.playerFight().doLastResult(new LastFightResultVo(player.playerFight().getFightId(),resultVo));
    	player.sendUserUpdateMsg();
    	player.sendMsg(MessageComm.S2C_Event_Fight,resultVo);
	}

	//派遣  （只能派遣到打过的点）
	@MsgMethod(MessageComm.C2S_Event_Dispatch)
    public void dispatch(Player player, JsonMsg msg) {
		int id = msg.getInt("id");//目的地
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
		if(template==null){
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(template.getWar_id());
		if(levelEvent==null){//没有这个事件
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		LevelEventNewTemplate nowTemplate = levelEventConfig.getLevelEventNew(levelEvent.getNowId());
		if(!nowTemplate.isNextId(id)){
			//没有连接，不能派遣
			return;
		}
		if (!levelEvent.isFighted(id)) {
			//没有占领，不能往回派遣
			return;
		}

		//派遣到该点
		levelEventBiz.dispatch(player, levelEvent, id, false);
		//增加步数
		levelEvent.addSteps();
    	player.sendUserUpdateMsg();
    	player.sendMsg(MessageComm.S2C_Event_Dispatch,id);
	}
	//呼叫支援
	@MsgMethod(MessageComm.C2S_Event_CallSupport)
    public void callSupport(Player player, JsonMsg msg) {
		int type = msg.getInt("type");//支援类型
		int warId = msg.getInt("warId");
		
		int id = msg.getInt("id");
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		if(levelEvent==null){//没有这个事件
			player.sendErrorMsg(SysConstant.Mission_Fight_Not);
			return ;
		}
		MissionEventWarTemplate template = levelEventConfig.getLevelEventWarNew(warId);
		if(template==null) {
			return;
		}
		//检查合法性
		if(!levelEventBiz.checkSuppor(player, warId, type, id)) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		int freeCount = template.getFree_help();
		long cost = levelEvent.getSupportCount()>=freeCount?levelEventBiz.getSupportCost(levelEvent.getSupportCount()-freeCount+1):0;
		if(cost>0&&!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Cash, cost, LogType.LevelEvent_CallSupport.value(warId))) {
			player.sendErrorMsg(SysConstant.PLAYER_Cash_NOT);
			return;
		}
		levelEventBiz.support(player, warId, type, id);
    	player.sendUserUpdateMsg();
    	player.sendMsg(MessageComm.S2C_Event_CallSupport,type);
	}
	
	/**
	 * 领取事件奖励
	 */
	@MsgMethod(MessageComm.C2S_Event_ReceiveReward)
	public void receiveTarget(Player player, JsonMsg msg){
		int id = msg.getInt("id");
		if(!player.playerEvent().canReceive(id)){
			return;
		}
		LevelEventRewardNewTemplate template = levelEventConfig.getRewardNew(id);
		List<Items> rewards = template.getRewards();
		//计算等级加成活动
    	rewards = activityEffectBiz.calActivityEffectItemsAdd(player, rewards, PlayerRewardMode.PlayerLvEvent);
		
		itemBiz.addItem(player, rewards, LogType.LevelEventRewardNew.value(id));
		player.playerEvent().receive(id);
		player.playerEvent().checkEvent();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Event_ReceiveReward,rewards);
	}
	/**
	 * 重置事件
	 */
	@MsgMethod(MessageComm.C2S_Event_Reset)
	public void resetNew(Player player, JsonMsg msg) {
		int warId = msg.getInt("warId");//代表哪一个战役
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		if(levelEvent==null){//没有这个事件
			return ;
		}
		player.playerEvent().reset(warId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Event_Reset, warId);

	}

	/**
	 * 选择事件
	 */
	@MsgMethod(MessageComm.C2S_Event_Choice)
	public void choice(Player player, JsonMsg msg) {
		int warId = msg.getInt("warId");//代表哪一个战役
		int choice = msg.getInt("choice");
		int point = msg.getInt("point");
		if (choice > 2) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		if (levelEvent == null) {//没有这个事件
			return;
		}
		if (!levelEvent.isEventRunning()) {
			return;
		}
		levelEventBiz.choiceEvent(player, levelEvent, choice, point);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Event_Choice, warId);

	}

	/**
	 * 选择起点
	 */
	@MsgMethod(MessageComm.C2S_Event_ChoiceStart)
	public void choiceStart(Player player, JsonMsg msg) {
		int warId = msg.getInt("warId");//代表哪一个战役
		int id = msg.getInt("id");
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		if (levelEvent == null) {//没有这个事件
			return;
		}
		if (levelEvent.getNowId() != 0) {
			//只有第一次才能选择起点
			return;
		}
		//检查该点是否是本战役的
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
		if (template == null || template.getWar_id() != warId) {
			//该点不存在或不是本战役的
			return;
		}
		levelEventBiz.dispatch(player, levelEvent, id, true);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Event_ChoiceStart, id);

	}
}
