package com.hm.action.guild;

import com.google.common.collect.Lists;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.guild.vo.ArmsVo;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.templaextra.GuildFactoryBuildExtraTemplate;
import com.hm.config.excel.templaextra.GuildFactoryPaperExtraTemplate;
import com.hm.config.excel.templaextra.ItemGuildWeaponExtraTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.bean.GuildPlayerVoWithCenterInfo;
import com.hm.model.item.Items;
import com.hm.model.player.*;
import com.hm.model.queue.ArmsQueue;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerFunction;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.centreArms.CentreArmsContainer;
import com.hm.servercontainer.centreArms.CentreArmsItemContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 * 军工厂(部落神兽)
 * @author xjt
 * @date 2020年4月11日16:58:57
 */
@Action
public class GuildFactoryAction extends AbstractGuildAction{
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildFactoryBiz guildFactoryBiz;
	@Resource
	private GuildFactoryConfig guildFactoryConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private LogBiz logBiz;
	//请求军工厂信息
	@MsgMethod(MessageComm.C2S_GuildFactory_Open)
	public void open(Player player, Guild guild, JsonMsg msg){
		player.sendMsg(MessageComm.S2C_GuildFactory_Open);
	}
	
	//建设
	@MsgMethod(MessageComm.C2S_GuildFactory_Build)
	public void build(Player player, Guild guild,  JsonMsg msg){
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		int type = msg.getInt("type");
		int count = msg.getInt("count");
		count = count > 0 ? count : 1;
		//根据建设类型获取建设消耗
		GuildFactoryBuildExtraTemplate template = guildFactoryConfig.getBuildTemplate(type);
		if(template==null){
			return;
		}

		List<Items> cost = template.getCost();
		List<Items> rewards = IntStream.range(0, count).mapToObj(i -> template.randomReward()).collect(Collectors.toList());
		rewards = ItemUtils.mergeItemList(rewards);
        if (count > 1) {
			cost = ItemUtils.calItemRateReward(cost, count);
		}
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.GuildBuild.value(count))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		guildFactoryBiz.build(player, guild, type, count);
		itemBiz.addItem(player, rewards, LogType.GuildBuild.value(count));
		guild.saveDB();
		guild.broadMemberGuildUpdate();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_GuildFactory_Build,rewards);
	}
	
	//生产(提交证书)
	@MsgMethod(MessageComm.C2S_GuildFactory_Produce)
	public void produce(Player player, Guild guild, JsonMsg msg){
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		int maxCount = commValueConfig.getCommValue(CommonValueType.GuildFactoryProduceLimit);
		if(player.playerGuild().getStrengthCount() > maxCount){
			return;
		}
		//根据建设类型获取建设消耗
		List<Items> cost = commValueConfig.getListItem(CommonValueType.GuildFactoryProductCost);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.GuildProduce)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		//每次增加5点繁荣度
        int index = guildFactoryBiz.produce(player, guild);
		player.playerGuild().produce();
		guild.saveDB();
		guild.broadMemberGuildUpdate();
		player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_GuildFactory_Produce, index);
	}
	
	//打开分配界面
	@MsgMethod(MessageComm.C2S_GuildFactory_OpenAllot)
	public void openAllot(Player player, Guild guild, JsonMsg msg){
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		if(!guild.isLeader(player) ) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		CentreArmsItemContainer centreArmsItemContainer = CentreArmsContainer.of(player);
		List<GuildPlayerVoWithCenterInfo> vos = guild.getGuildMembers().getGuildMembers().stream()
				.map(e ->GuildPlayerVoWithCenterInfo.buildVo(e)).collect(Collectors.toList());
		for (GuildPlayerVoWithCenterInfo vo : vos) {
			//找出有空位可以放图纸的
			CentreArms centre = centreArmsItemContainer.getCentreArms(vo.getPlayerId());
			if (centre != null) {
				vo.setArms(centre);
			}
		}
		player.sendMsg(MessageComm.S2C_GuildFactory_OpenAllot,vos);
	}
	
	//分配
	@MsgMethod(MessageComm.C2S_GuildFactory_Allot)
	public void allot(Player player, Guild guild, JsonMsg msg){
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		if(!guild.isLeader(player) ) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		int index = msg.getInt("index");//分配第几个图纸(从0开始)
		long playerId = msg.getInt("playerId");//目标玩家id
		Player targetPlayer = PlayerUtils.getPlayer(playerId);
		if(targetPlayer==null){
			return ;
		}
		if(index<0||index>2){
			return;
		}
		//该位置是否有生产完的图纸
		int paperId = guild.guildFactory().getDrawByIndex(index);
		if(paperId<=0){
			player.sendErrorMsg(SysConstant.GuildArms_Draw_Not);
			return;
		}
		CentreArmsItemContainer centreArmsItemContainer = CentreArmsContainer.of(player);
		//检查该玩家是否有空闲生产槽位
		CentreArms centreArms = centreArmsItemContainer.getCentreArms(playerId);
		if(centreArms==null){
			centreArms = new CentreArms(targetPlayer);
			centreArmsItemContainer.addCentreArms(centreArms);
		}
		int pos = centreArms.getEmptyPos();
		if(pos<0){
			player.sendErrorMsg(SysConstant.GuildArms_Player_Full);
			return;
		}
		synchronized (centreArms) {
			//分配给玩家
			centreArms.addPaper(paperId,pos);
			centreArms.save();
		}
		guild.guildFactory().allot(index);
		//分配完毕后检查繁荣度是否可以再生成一张图纸
		guild.guildFactory().checkProsperity();
		guild.guildFactory().addAllotRecord(player,targetPlayer, paperId);
		guild.saveDB();
		guild.broadMemberGuildUpdate();
		logBiz.addPlayerActionLog(player, ActionType.ArmsPaperAllot.getCode(), targetPlayer.getId()+"_"+paperId);
		//通知玩家武器位发生变化
		guildFactoryBiz.sendArmsPosChange(targetPlayer);
		player.sendMsg(MessageComm.S2C_GuildFactory_Allot);
	}

    //销毁图纸
    @MsgMethod(MessageComm.C2S_GuildFactory_Destory)
    public void destory(Player player, Guild guild, JsonMsg msg) {
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
        if (!guild.isLeader(player)) {
            player.sendErrorMsg(SysConstant.Guild_NoPower);
            return;
        }
        int index = msg.getInt("index");//分配第几个图纸(从0开始)
        //该位置是否有生产完的图纸
        int paperId = guild.guildFactory().getDrawByIndex(index);
        if (paperId <= 0) {
            player.sendErrorMsg(SysConstant.GuildArms_Draw_Not);
            return;
        }
        guild.guildFactory().allot(index);
        //分配完毕后检查繁荣度是否可以再生成一张图纸
        guild.guildFactory().checkProsperity();
        guild.guildFactory().addAllotRecord(player, null, paperId);
        guild.saveDB();
        guild.broadMemberGuildUpdate();
        logBiz.addPlayerActionLog(player, ActionType.ArmsPaperDestory.getCode(), paperId + "");
        player.sendMsg(MessageComm.S2C_GuildFactory_Destory);
    }
	//建造武器(得到图纸后手动开始建造)
	@MsgMethod(MessageComm.C2S_GuildFactory_BuildArms)
	public void buildArms(Player player, Guild guild, JsonMsg msg){
		if(player.playerArms().getState()==0){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		int index = msg.getInt("index");
		if(index<0||index>4){
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		CentreArms centreArms = CentreArmsContainer.of(player).getCentreArms(player.getId());
		ArmsPosition armsPosition = centreArms.getArms(index);
		if(armsPosition==null||armsPosition.getState()!=0){
			//该位置武器状态不对，不能开始建造
			return;
		}
		GuildFactoryPaperExtraTemplate template = guildFactoryConfig.getPaper(armsPosition.getId());
		if(template==null){
			return;
		}
		int minutes = template.getBuild_time();
		armsPosition.buildStart(minutes);
		centreArms.save();
		//添加生产队列
		ArmsQueue queue = new ArmsQueue(player,index,armsPosition.getId(),minutes*60);
	    player.playerQueue().addQueue(queue);
	    //通知玩家武器位发生变化
	  	guildFactoryBiz.sendArmsPosChange(player);
	    player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_GuildFactory_BuildArms);
	}
	//强化
	@MsgMethod(MessageComm.C2S_GuildFactory_Strength)
	public void strength(Player player, Guild guild, JsonMsg msg){
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		String id = msg.getString("id");
		int index = msg.getInt("index");
		Arms arms = player.playerArms().getArms(id);
		if(arms==null){
			return;
		}
		if(index<0||index>2){
			return;
		}
		int maxLv = guildFactoryConfig.getMaxLv();
		if(arms.getLv()>=maxLv){
			return;
		}
		//判断该武器是否已经在强化位上
		if(guild.guildFactory().isInStrengthPos(id)){
			return;
		}
		int countLimit = commValueConfig.getCommValue(CommonValueType.GuildFactoryStrengthLimt);
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.ArmsStrengthCount)>=countLimit){
			return;
		}
		ArmsVo vo = guild.guildFactory().getArmsByIndex(index);
		if(vo!=null){
			//该位置上已有正在强化的武器
			return;
		}
		int cost = commValueConfig.getCommValue(CommonValueType.GuildFactoryStrengthCost);
		//判断部落是否有位置

		if(guild.guildFactory().getParts()<cost){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		guild.guildFactory().strength(player,index,arms);
		GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(player.getId());
		if(guildPlayer!=null){
			guildPlayer.strength();
			guild.getGuildMembers().SetChanged();
		}
		guild.saveDB();
		guild.broadMemberGuildUpdate();
		guildBiz.sendGuildMember(player);
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.ArmsStrengthCount);
		player.sendUserUpdateMsg();
		logBiz.addPlayerActionLog(player, ActionType.ArmsStrength.getCode(), id+"_"+arms.getId());
		player.sendMsg(MessageComm.S2C_GuildFactory_Strength);

	}
	
	//强化完成(从强化位置上卸除)
	@MsgMethod(MessageComm.C2S_GuildFactory_StrengthFinish)
	public void strengthFinish(Player player, Guild guild, JsonMsg msg){
		if(!guildFactoryBiz.isGuildFactoryUnlock(guild)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		int index = msg.getInt("index");
		ArmsVo armsVo = guild.guildFactory().getArmsByIndex(index);
		if(armsVo==null||armsVo.getEndTime()>System.currentTimeMillis()){
			return;
		}
		int exp = commValueConfig.getCommValue(CommonValueType.GuildFactoryExpOnce);
		Player targetPlayer = PlayerUtils.getPlayer(armsVo.getPlayerId());
		Arms arms = targetPlayer.playerArms().getArms(armsVo.getUid());
		if(arms==null){
			return;
		}
		boolean lvUp = arms.addExp(exp);
		if(arms.getPos()>=0){
			//更新redis中数据
			//检查该玩家是否有空闲生产槽位
			CentreArms centreArms = CentreArmsContainer.of(player).getCentreArms(targetPlayer.getId());
			centreArms.up(arms.getPos(), arms);
			centreArms.save();
		}
		if(lvUp){
			//发出信号更新玩家属性
			targetPlayer.notifyObservers(ObservableEnum.ArmsChange);
		}
		targetPlayer.playerArms().SetChanged();
		//从部落强化位上拿下来
		guild.guildFactory().strengthDown(index);
		guild.saveDB();
		guild.broadMemberGuildUpdate();
		guildFactoryBiz.sendArmsPosChange(targetPlayer);
		targetPlayer.sendUserUpdateMsg();
		logBiz.addPlayerActionLog(player, ActionType.ArmsStrengthFinish.getCode(), targetPlayer.getId()+"_"+arms.getUid()+"_"+arms.getId());
		player.sendMsg(MessageComm.S2C_GuildFactory_StrengthFinish);
	}
	
	//武器上阵或替换
	@MsgMethod(MessageComm.C2S_GuildFactory_ArmsUp)
	public void up(Player player, Guild guild, JsonMsg msg){
		if(player.playerArms().getState()!=1){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		CentreArmsItemContainer centreArmsItemContainer =  CentreArmsContainer.of(player);
		CentreArms centreArms = centreArmsItemContainer.getCentreArms(player.getId());
		if(centreArms==null){
			centreArms = new CentreArms(player);
			centreArmsItemContainer.addCentreArms(centreArms);
			centreArms.save();
		}
		int index = msg.getInt("index");
		if(index>centreArms.getUnlockNum()){
			//位置未开放
			return;
		}
		String id = msg.getString("id");
		Arms arms = player.playerArms().getArms(id);
		if(arms==null||arms.getPos()>0){
			//武器不存在或已经装备上
			return;
		}
		ItemGuildWeaponExtraTemplate template = guildFactoryConfig.getWeapon(arms.getId());
		if(template==null){
			return;
		}
		//判断是否有同类型的武器（本位置不进行判断）
		if(centreArms.isHaveThisTypeWithOutPos(template.getType(),index)){
			return;
		}
		
		//上阵或替换
		ArmsPosition armsPosition = centreArms.getArms(index);
		if(armsPosition!=null&&armsPosition.getState()!=2){
			//正在建造中或还是图纸不能上阵到此位置
			return;
		}
		if(armsPosition!=null){
			//下阵原来的
			player.playerArms().down(armsPosition.getArms().getUid());
		}
		//把新的上去
		arms.up(index);
		centreArms.up(index,arms);
		centreArms.save();
		player.playerArms().SetChanged();
		player.notifyObservers(ObservableEnum.ArmsChange);
		guildFactoryBiz.sendArmsPosChange(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_GuildFactory_ArmsUp);
	}
	
	//武器下阵
	@MsgMethod(MessageComm.C2S_GuildFactory_ArmsDown)
	public void down(Player player, Guild guild, JsonMsg msg){
		if(player.playerArms().getState()!=1){
			return;
		}
		CentreArms centreArms = CentreArmsContainer.of(player).getCentreArms(player.getId());
		int index = msg.getInt("index");
		//上阵或替换
		ArmsPosition armsPosition = centreArms.getArms(index);
		if(armsPosition==null){
			return;
		}
		if(index>centreArms.getUnlockNum()){
			//位置未开放
			return;
		}
		if(armsPosition.getState()!=2){
			return;
		}
		String armsId = armsPosition.getArms().getUid();
		centreArms.down(index);
		centreArms.save();
		player.playerArms().down(armsId);
		player.notifyObservers(ObservableEnum.ArmsChange);
		guildFactoryBiz.sendArmsPosChange(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_GuildFactory_ArmsDown);
	}
	
	//购买位置
	@MsgMethod(MessageComm.C2S_GuildFactory_BuyPos)
	public void buyPos(Player player, Guild guild, JsonMsg msg){
		if(player.playerArms().getState()!=1){
			return;
		}
		CentreArmsItemContainer  centreArmsItemContainer  = CentreArmsContainer.of(player);
		CentreArms centreArms = centreArmsItemContainer.getCentreArms(player.getId());
		if(centreArms==null){
			centreArms = new CentreArms(player);
			centreArmsItemContainer.addCentreArms(centreArms);
			centreArms.save();
		}
		if(centreArms.getUnlockNum()>=4){
			return;
		}
		int[] prices = commValueConfig.getCommonValueByInts(CommonValueType.ArmsOpenCost);
		if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, prices[centreArms.getUnlockNum()], LogType.BuyArmsPos)){
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		centreArms.unlockPos();
		centreArms.save();
		guildFactoryBiz.sendArmsPosChange(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_GuildFactory_BuyPos);
	}
	
	//分解
	@MsgMethod(MessageComm.C2S_Arms_Decompose)
	public void decompose(Player player, Guild guild, JsonMsg msg){
		String uid = msg.getString("id");
		Arms arms = player.playerArms().getArms(uid);
		if(arms==null||arms.getPos()>=0){
			//武器不存在或武器在上阵位上
			return;
		}
		//分解
		ItemGuildWeaponExtraTemplate guildWeaponTemplate = guildFactoryConfig.getWeapon(arms.getId());
		//把经验转换为材料
		int exp = guildFactoryConfig.getExpTotalLv(arms.getId(), arms.getLv())+arms.getExp();
		int onceExp = commValueConfig.getCommValue(CommonValueType.GuildFactoryExpOnce);
		int count = (int) MathUtils.div(MathUtils.mul(exp, guildWeaponTemplate.getRecycle_rate()), onceExp);
		List<Items> rewards = guildWeaponTemplate.getRecycleRewards();
		if(count>0){
			Items items = new Items(GameConstants.Guild_Factory_RepairId,count,ItemType.ITEM);
			rewards.add(items);
		}
		itemBiz.addItem(player, rewards, LogType.ArmsDecompose.value(arms.getId()));
		logBiz.addPlayerActionLog(player, ActionType.ArmsStrength.getCode(), arms.getId()+"");
		player.playerArms().decompose(uid);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Arms_Decompose,rewards);
	}

	
	//维修训练
	@MsgMethod(MessageComm.C2S_Repair_Train)
	public void repairTrain(Player player, Guild guild, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.RepairTrain.getType())){
			return;
		}
		int score = msg.getInt("score");
		int type = msg.getInt("type");
		int scoreLimit = commValueConfig.getCommValue(CommonValueType.RepairTrainScoreLimit);
		if(score>scoreLimit){
			//玩家作弊
			player.sendErrorMsg(SysConstant.RepairTrain_Score_Invalid);
			score = 0;
		}
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.RepairTrainFinishCount)>0){
			List<Items> cost = commValueConfig.getListItem(CommonValueType.RepairTrainCost);
			if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.RepairTrain)){
				player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
				return;
			}
		}
		if(player.playerRepairTrain().getScore()<score){
			HdLeaderboardsService.getInstance().updatePlayerRankOverrideForTime(player, RankType.RepairTrain, score);
		}
		player.playerRepairTrain().updateScore(score);
		List<Items> rewards = Lists.newArrayList();
		if(type==1&&player.getPlayerStatistics().getTodayStatistics(StatisticsType.RepairTrainFinishCount)<=0){
			rewards = commValueConfig.getListItem(CommonValueType.RepairTrainFirstReward);
			itemBiz.addItem(player, rewards, LogType.RepairTrain);
		}
		if(type==1){
			player.getPlayerStatistics().addTodayStatistics(StatisticsType.RepairTrainFinishCount);
		}
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.RepairTrainCount);
		player.sendUserUpdateMsg();
		JsonMsg returnMsg = JsonMsg.create(MessageComm.S2C_Repair_Train);
		returnMsg.addProperty("score", score);
		returnMsg.addProperty("rewards", rewards);
		player.sendMsg(returnMsg);
	}
	
	
	//打开记录
	@MsgMethod(MessageComm.C2S_GuildFactory_OpenRecord)
	public void openRecord(Player player, Guild guild, JsonMsg msg){
		int type = msg.getInt("type");
		JsonMsg returnMsg = JsonMsg.create(MessageComm.S2C_GuildFactory_OpenRecord);
		returnMsg.addProperty("type", type);
		switch(type){
			case 1:
				returnMsg.addProperty("record", guild.guildFactory().createBuildRecordVo());
				break;
			case 2:
				returnMsg.addProperty("record", guild.guildFactory().createProduceRecordVos());
				break;
			case 3:
				returnMsg.addProperty("record", guild.guildFactory().createAllotRecordVos());
				break;
		}
		player.sendMsg(returnMsg);
	}

}
