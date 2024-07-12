	
package com.hm.action.build;


import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.build.biz.BuildBiz;
import com.hm.action.cd.biz.PlayerCdBiz;
import com.hm.action.http.biz.HttpBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.config.BuildConfig;
import com.hm.enums.AttributeType;
import com.hm.enums.BuildQueueType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.queue.AbstractQueue;
import com.hm.rmi.LogServerRmiHandler;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: BuildAction.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年5月5日 上午9:51:21
 * @version 1.0
 */
@Action
public class BuildAction extends AbstractPlayerAction{
	@Resource
	private BuildBiz buildBiz;
	@Resource
	private LogServerRmiHandler logServerRmiHandler;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private PlayerCdBiz playerCdBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private HttpBiz httpBiz;
	@Resource
	private QueueBiz queueBiz;
	
	/**
	 * 建筑物建造
	 * @param session
	 * @param req
	 */
	@MsgMethod(MessageComm.C2S_BuildCreate)
	public void buildCreate(Player player,JsonMsg msg){
		int id = msg.getInt("id");//建筑唯一id
		int blockId = msg.getInt("blockId");//地块ID
		//判断该建筑是否已经存在
		int lv = player.playerBuild().getBuildLv(id);
		if(lv>0){//建筑已经存在
			return;
		}
		//判断该位置是否已经有建筑
		int buildId = player.playerBuild().getBuildId(blockId);
		if(buildId>0){//该位置已有建筑
			return;
		}
		//判断该建筑是否应该建造在此数
		if(!buildConfig.checkBuildBlock(id,blockId)){
			return;
		}
		//获取空闲的建筑队列
		BuildQueueType freeQueue = queueBiz.getFreeBuildQueue(player);
		if(freeQueue==BuildQueueType.None){//没有空闲的建筑队列
			player.sendErrorMsg(SysConstant.BUILD_QUEUE_FULL);
			return;
		}
		//根据id获取建筑类型
		int type = buildConfig.getBuildType(id);
		if(!buildBiz.checkBuildLvUpCost(type, 0, player)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		buildBiz.buildLvUpStart(id,type,freeQueue,0,player);
		player.playerBuild().createBuild(blockId, id,0);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_BuildCreate);
	}
	
	/**
	 * 建筑物升级统一方法
	 * @param session
	 * @param req
	 */
	@MsgMethod(MessageComm.C2S_BuildLvUp)
	public void buildinglvup(Player player,JsonMsg msg){
		int id = msg.getInt("id");//建筑唯一id
		//判断该建筑上是否有队列正在进行中
		List<AbstractQueue> queues = player.playerQueue().getQueueByBuildId(id);
		if(queues.size()>0){
			player.sendErrorMsg(SysConstant.BUILD_TIME);
			return;
		}
		//获取空闲的建筑队列
		BuildQueueType freeQueue = queueBiz.getFreeBuildQueue(player);
		if(freeQueue==BuildQueueType.None){//没有空闲的建筑队列
			player.sendErrorMsg(SysConstant.BUILD_QUEUE_FULL);
			return;
		}
		
		//根据唯一id获取建筑等级
		int lv = player.playerBuild().getBuildLv(id);
		int type =buildConfig.getBuildType(id);
		//等级上限判断
		if(!buildBiz.isCanlvUp(player,type,lv)){
			player.sendErrorMsg(SysConstant.LV_MAX);
			return;
		}
		if(!buildBiz.checkBuildLvUpCost(type, lv, player)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
			
		}
		buildBiz.buildLvUpStart(id,type,freeQueue,lv,player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_BuildLvUp);
	}
	
	/**
	 * 立刻建造升级
	 * @param session
	 * @param req
	 */
	/*@MsgMethod(MessageComm.C2S_BuildinglvupNow)
	public void buildinglvupNow(Player player,JsonMsg msg){
		int blockId = msg.getInt("blockId");
		BuildUpQueue queue = player.playerQueue().getQueueByBlockIdAndQueueType(blockId,QueueType.BuildUp);
		//判断是正在升级
		if(queue!=null&&!QueueState.isRunning(queue.getState())){
			player.sendErrorMsg(SysConstant.QUEUE_SPEED_UP_OVER);
			return;
		}
		int buildType = player.getBuildTypeByBlockId(blockId);
		int lv = player.getBuildLvByBlockId(blockId);
		if(buildType<=0||lv<=0){
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		//等级上限判断
		if(!playerBiz.isCanlvUp(player,buildType,lv)){
			player.sendErrorMsg(SysConstant.LV_MAX);
			return;
		}
		//升级
		int costDiamonds = buildBiz.getBuildLvUpGoldCost(buildType,lv,player);
		if(!player.playerCurrency().canSpend(CurrencyKind.Diamonds,costDiamonds)){
			player.sendErrorMsg( SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		//扣金币
		playerBiz.spendPlayerCurrency(player, CurrencyKind.Diamonds, costDiamonds, LogType.NOWUP_OUT);
		//扣资源
		buildBiz.reduceBuildLvUpResource(buildType, lv, player);
		player.notifyObservers(ObservableEnum.BuildLevelUpStart, blockId,"");
		player.buildLvUp(blockId);
		player.sendUserUpdateMsg();
	}*/
	/**
	 * 收矿
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_Build_Res_Collect)
	public void resCollect(Player player,JsonMsg msg) {
		if(player.playerBuild().getProductTime()<1){
			return;
		}
		long num = buildBiz.getResCollect(player);
		long limit = (long) player.getPlayerDynamicData().getMaxResLimit(AttributeType.Crystal_Limit);
		if(limit<player.playerCurrency().get(CurrencyKind.Crystal)) {
			player.sendErrorMsg(SysConstant.Res_Limit);
			return;
		}
		
		playerBiz.addPlayerCurrency(player, CurrencyKind.Crystal, num, LogType.Res_Collect);
		player.playerBuild().setResCollectTime(System.currentTimeMillis());
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Build_Res_Collect,num);
	}
	
	/**
	 * 建筑交换位置
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_Build_ChangeBlock)
	public void changeBlock(Player player,JsonMsg msg) {
		int blockId = msg.getInt("blockId");
		int blockId2 = msg.getInt("blockId2");
		player.playerBuild().changeBlockId(blockId, blockId2);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Build_ChangeBlock);
	}
	
	/**
	 * 自动建造开启关闭
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_Build_Auto)
	public void autoBuild(Player player,JsonMsg msg) {
		int type = msg.getInt("type");//0-关闭 1-开启
		player.playerBuild().autoBuild(type);
		buildBiz.checkAutoBuild(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Build_Auto,type);
	}
}

