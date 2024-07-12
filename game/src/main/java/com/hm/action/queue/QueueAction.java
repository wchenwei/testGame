package com.hm.action.queue;


import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.bag.BagBiz;
import com.hm.action.build.biz.BuildBiz;
import com.hm.action.build.vo.CollectionQueueVO;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.QueueState;
import com.hm.enums.QueueType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.queue.AbstractQueue;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class QueueAction extends AbstractPlayerAction{
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BuildBiz buildBiz;
	@Resource
	private BagBiz bagBiz;
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private QueueBiz queueBiz;
	@Resource
	private VipBiz vipBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CommValueConfig commValueConfig;
	/**
	 * 收取队列
	 * @param session
	 * @param req
	 */
	@MsgMethod(MessageComm.C2S_Queue_CollectionProduct)
	public void collectionProduct(Player player,JsonMsg msg){
		String queueId = msg.getString("queueId");
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		//判断是正在升级
		if(queue==null||!queue.isComplete()){//生产或升级未完成
			//player.sendErrorMsg(SysConstant.QUEUE_PRODUCE_NOT_COMPLETE);
			return;
		}
		/*if(queue.getQueueType()==QueueType.PartProduce&&partsBiz.isFull(player)){
			player.sendErrorMsg(SysConstant.Parts_Full);
			return;
		}*/
		CollectionQueueVO vo = queue.collectionProduct(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Queue_CollectionProduct, vo);
	}
	
	/**
	 * 队列时间到后请求
	 * @param session
	 * @param req
	 */
	@MsgMethod(MessageComm.C2S_Queue_TimeOver)
	public void queueTimeOver(Player player,JsonMsg msg){
		String queueId = msg.getString("queueId");
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		if(queue==null){
			return;
		}
		if(queue.isOver()){
			player.playerQueue().queueComplete(queueId);
			player.sendUserUpdateMsg();
		}else{
			//再发一次队列信息给客户端
			player.playerQueue().SetChanged();
			player.sendUserUpdateMsg();
		}
	}
	
	/**
	 * 队列加速
	 * @param session
	 * @param req
	 */
	@MsgMethod(MessageComm.C2S_Queue_SpeedUp)
	public void queueSpeedUp(Player player,JsonMsg msg){
		String queueId = msg.getString("queueId");
		int type = Math.max(1, msg.getInt("type"));//1-使用钻石直接加速 2-使用道具加速
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		if(queue==null){
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		//是否在升级中
		if(!QueueState.isRunning(queue.getState())){
			player.sendErrorMsg(SysConstant.QUEUE_PRODUCE_SPEED_NOT);
			return;
		}
		if(type==1){//钻石直接加速
			if(!QueueType.isCanSpeedUp(queue.getQueueType())){
				player.sendErrorMsg(SysConstant.QUEUE_PRODUCE_SPEED_NOT);
				return;
			}
			//剩余时间（分钟）
			int costTime = queueBiz.getCostTime(queue.getEndTime());
			int costDiamonds = costTime*queue.getQueueType().getCost();
			costDiamonds = DateUtil.getRemainTime(queue.getEndTime())<=commValueConfig.getCommValue(CommonValueType.queueSpeedUpFreeTime)?0:costDiamonds;
			if(costDiamonds<=0||playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, costDiamonds, LogType.QueueSpeed)){
				queue.setEndTime(System.currentTimeMillis());//将该队列的完成时间改为当前时间
				player.playerQueue().queueComplete(queueId);
			}else{
				player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
				return;
			}
		}else if(type==2){//使用道具加速
			if(!QueueType.isCanSpeedUpUserItem(queue.getQueueType())){
				player.sendErrorMsg(SysConstant.QUEUE_PRODUCE_SPEED_NOT);
				return;
			}
			int itemId = msg.getInt("itemId");
			int num = msg.getInt("num");
			ItemTemplate itemTemplate = itemConfig.getItemTemplateById(itemId);
			if(itemTemplate==null){
				player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
				return;
			}
			if(!bagBiz.spendItem(player, itemId, num, LogType.QueueSpeed)){
				player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
				return;
			}
			//int effectValue = itemTemplate.getEffectValue();
			int effectValue = 30;
			player.playerQueue().queueSpeed(queueId, effectValue*num);
		}
		player.sendUserUpdateMsg();
	}
	/**
	 * 建筑队列免费加速
	 * @param session
	 * @param req
	 */
	/*@MsgMethod(MessageComm.C2S_QueueSpeedUp_Free)
	public void queueSpeedUpFree(Player player,JsonMsg msg){
		String queueId = msg.getString("queueId"); 
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		if(queue==null){
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		//是否在升级中
		if(!QueueState.isRunning(queue.getState())){
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		if(queue.getQueueType()!=QueueType.BuildUp||queue.isFreeFast()){//不是建筑队列或者已经使用过vip加速
			return;
		}
		//从VIP信息内获得免费加速时间
		int time = vipBiz.getVipPow(player, VipPowType.BuildSpeed);
		player.playerQueue().freeSpeed(queueId, time);
		player.sendUserUpdateMsg();
	}*/
	/**
	 * 坦克生产等待队列取消
	 * @param player
	 * @param msg
	 */
	/*@MsgMethod(MessageComm.C2S_Queue_Cancel)
	public void cancelQueue(Player player,JsonMsg msg) {
		String queueId = msg.getString("queueId");
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		if(queue==null||queue.getState()!=QueueState.Wait){//只有等待中的队列才能取消
			player.sendErrorMsg(SysConstant.QUEUE_CANCEL_NOT);
			return;
		}
		//删除升级队列
		queueBiz.deleteQueue(player, queue);
		//返还资源
		tankFactoryBiz.returnTankRe((TankProduceQueue)queue,player);
		player.sendUserUpdateMsg();
	}*/
	
	/**
	 * 事件加速
	 * @param player
	 * @param msg
	 */
	/*@MsgMethod(MessageComm.C2S_EventSpeedUp)
	public void eventSpeedUp(Player player,JsonMsg msg) {
		String queueId = msg.getString("queueId");
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		if(queue==null){
			player.sendErrorMsg(SysConstant.SYS_ERROR);
			return;
		}
		if(queue.getState()!=QueueState.Run){
			player.sendErrorMsg(SysConstant.QUEUE_CAN_NOT_SPEED_UP);
			return;
		}
		if(queue.getEventSpeedSecond()<=0){
			player.sendErrorMsg(SysConstant.QUEUE_CAN_NOT_SPEED_UP);
			return;
		}
		player.playerQueue().eventSpeed(queueId);
		player.sendUserUpdateMsg();
	}*/
	
	/**
	 * 购买建筑队列
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_Queue_Buy)
	public void queueBuy(Player player,JsonMsg msg) {
		int maxQueueNum = 3;
		if(player.playerQueue().getQueueNum()>=maxQueueNum){
			return;
		}
		int cost = player.playerQueue().getBuyCost();
		if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, cost,LogType.Queue_Buy)){
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		player.playerQueue().buyQueue();
		buildBiz.checkAutoBuild(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Queue_Buy);
	}
	@MsgMethod(MessageComm.C2S_Queue_Cancel)
	public void queueCancel(Player player,JsonMsg msg){
		String queueId = msg.getString("queueId");
		AbstractQueue queue = player.playerQueue().getQueueById(queueId);
		if(!QueueType.isCanCancel(queue.getQueueType())){
			return;
		}
		if(queue.isOver()){
			return;
		}
		//小于1分钟不能取消
		/*if(queue.getLastSecond()<GameConstants.SECOND*30){
			return;
		}*/
		List<Items> returnRes = queueBiz.getQueueCancelReturn(player, queue);
		itemBiz.addItem(player, returnRes, LogType.Queue_Cancel);
		int buildId = queue.getBuildId();
		if(queue.getQueueType()==QueueType.BuildUp&&player.playerBuild().getBuildLv(buildId)==0){
			player.playerBuild().deleteBuild(buildId);
		}
		player.playerQueue().deleteQueue(queue);
		buildBiz.checkAutoBuild(player);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Queue_Cancel,returnRes);
	}
	
	
	
}
