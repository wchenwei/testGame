package com.hm.action.queue.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.count.CountUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.build.biz.BuildBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.BuildConfig;
import com.hm.config.excel.templaextra.BuildUpTemplate;
import com.hm.enums.BuildQueueType;
import com.hm.enums.QueueState;
import com.hm.enums.QueueType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.queue.AbstractQueue;
import com.hm.model.queue.BuildUpQueue;
import com.hm.model.queue.ProduceQueue;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Biz
public class QueueBiz implements IObserver{
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	@Resource
	private BuildBiz buildBiz;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private ItemBiz itemBiz;

	public void deleteQueue(BasePlayer player,AbstractQueue queue){
		player.playerQueue().deleteQueue(queue);
	}

	/*//获取该生产坦克队列的状态,如果返回null说明没有可用队列
	public QueueState getProductTankState(Player player, BaseTankFactory factory) {
		List<AbstractQueue> queueList = player.playerQueue().getQueueByBlockId(factory.getBlockId(),QueueType.TankProduce);
		if(queueList.size()>=factory.getExtensionTimes()+1){
			return null;
		}else if(queueList.isEmpty()){
			return QueueState.Run;
		}else{
			return QueueState.Wait;
		}
	}*/
	//给队列添加加速事件
	/*public void speedEvent(Player player,int eventType,int num){
		QueueType queueType = QueueType.getQueueTypeByEventType(eventType);
		if(queueType==null){
			return ;
		}
		AbstractQueue queue = getRandomEventSpeedQueue(player, queueType);
		if(queue==null){
			return;
		}
		player.playerQueue().queueEventSpeed(queue.getId(), num);
		player.sendUserUpdateMsg();
		
	}*/
	//获取该队列的下一个等待队列
	public AbstractQueue getNextWaitQueue(BasePlayer player,int buildId,QueueType queueType){
		List<AbstractQueue> queueList = player.playerQueue().getQueueByBuildIdAndQueeType(buildId, queueType);
		Collections.sort(queueList, new Comparator<AbstractQueue>() {
			@Override
			public int compare(AbstractQueue o1, AbstractQueue o2) {
				return (int) (o1.getStartTime() - o2.getStartTime());
			}
		});
		for(AbstractQueue queue: queueList){
			if(queue.getState()==QueueState.Wait){
				return queue;
			}
		}
		return null;
	}
	//是否可以自动建造
	/*public boolean isCanAutoBuild(int buildQueuType,BasePlayer player){
		if(!player.getPlayerAutoInfo().isCanAutoLevelUp()){
			return false;
		}
		if(buildQueuType==BuildQueueType.Normal.getType()||buildQueuType==BuildQueueType.Business.getType()&&player.playerBuffer().haveBuff(BuffType.BusinessQueue)){
			return true;
		}
		return false;
	}*/

	//获取材料生产队列位置的预设队列
	/*public MaterialQueue getMaterialNextQueue(BasePlayer player,int index){
		List<AbstractQueue> queueList = player.playerQueue().getQueueByTypeAndState(QueueType.MaterialProduce, QueueState.Wait);
		for(int i=queueList.size()-1;i>=0;i--){
			MaterialQueue queue = (MaterialQueue)queueList.get(i);
			if(queue.getIndex()==index){
				return queue;
			}
		}
		return null;
	}*/
	/**
	 * 选取添加随机事件队列加速的队列
	 * @param player
	 * @param queueType
	 * @return
	 */
	public AbstractQueue getRandomEventSpeedQueue(BasePlayer player,QueueType queueType){
		List<AbstractQueue> queueList = player.playerQueue().getQueueByTypeAndState(queueType,QueueState.Run);
		if(queueList.isEmpty()){
			return null;
		}
		List<AbstractQueue> randomQueueList = Lists.newArrayList(queueList);
		Collections.shuffle(randomQueueList);
		//修改为纯随机
		/*for(int i=randomQueueList.size()-1;i>=0;i--){
			AbstractQueue queue = randomQueueList.get(i);
			if(!queue.isEventSpeed()){//没有事件加速在队列身上，就是它了
				return queue;
			}
		}*/
		return randomQueueList.get(0);
	}
	//获取当前空闲的建筑队列
	public BuildQueueType getFreeBuildQueue(Player player) {
		List<BuildQueueType> freeQueues = Lists.newArrayList(BuildQueueType.values());
		freeQueues.remove(BuildQueueType.None);
		List<AbstractQueue> buildQueues = player.playerQueue().getQueueByType(QueueType.BuildUp);
		freeQueues = freeQueues.stream().filter(t ->{
			return !buildQueues.stream().anyMatch(queue -> {
				BuildUpQueue q=(BuildUpQueue)queue;
				return q.getBuildQueueType()==t.getType();
			});
		}).collect(Collectors.toList());
		int queueNum = player.playerQueue().getQueueNum();
		Optional<BuildQueueType> optional= freeQueues.stream().filter(t ->queueNum>=t.getType()).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}
		return BuildQueueType.None;
	}

	@Override
	public void registObserverEnum() {
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
					   Object... argv) {
	}

	// 计算距离该时间还有多少分钟
	public int getCostTime(long endTime) {
		return CountUtil.ceil(DateUtil.getRemainTime(endTime)/60d);
	}

	//获取队列取消返还资源
	public List<Items> getQueueCancelReturn(Player player,AbstractQueue queue){
		int buildId = queue.getBuildId();
		int lv = player.playerBuild().getBuildLv(buildId);
		QueueType queueType = queue.getQueueType();
		switch(queueType){
			case BuildUp:
				return buildBiz.getBuildQueueRe(buildId,lv);
			case Produce:
				ProduceQueue produceQueue = (ProduceQueue)queue;
				return getProduceQueueRe(buildId,lv,produceQueue.getIndex(),produceQueue.getNum());
		}
		return Lists.newArrayList();
	}

	public List<Items> getProduceQueueRe(int buildId,int lv,int index,int num){
		int buildType = buildConfig.getBuildType(buildId);
		BuildUpTemplate template = buildConfig.getBuildUpTemplate(buildType, lv);
		if(template!=null){
			return ItemUtils.calItemRateReward(ItemUtils.calItemRateReward(template.getProductCost(index), num),0.5);
		}
		return Lists.newArrayList();
	}

	//获取可掠夺的建筑id列表
	public static List<Integer> getPlunderBuild(Player player){
		List<AbstractQueue> list = player.playerQueue().getQueueByTpyeAndState(QueueType.Produce, QueueState.Complete);
		return list.stream().map(t ->t.getBuildId()).collect(Collectors.toList());
	}

	/**
	 * 检查玩家队列
	 * @param player
	 */
	public void checkPlayerQueue(Player player) {
		List<AbstractQueue> queueList=player.playerQueue().getQueues();
		for(int i=queueList.size()-1;i>=0;i--) {
			AbstractQueue queue=queueList.get(i);
			if(queue.isOver()) {// 时间到
				player.playerQueue().queueComplete(queue.getId());
			}
		}
		//只更新不发送
		player.saveDB();
	}

	//掠夺
	public List<Items> plunder(Player player,List<Integer> buildIds){
		List<Items> rewards = Lists.newArrayList();
		List<String> deleteQueue = Lists.newArrayList();
		List<AbstractQueue> list = player.playerQueue().getQueueByTpyeAndState(QueueType.Produce, QueueState.Complete);
		list.forEach(t ->{
			if(buildIds.contains(t.getBuildId())) {
				ProduceQueue queue = (ProduceQueue)t;
				Items reward = queue.plunder();
				if(reward!=null) {
					rewards.add(reward);
				}
				if(queue.getProduct().getCount()<=0) {
					deleteQueue.add(queue.getId());
				}
			}
		});
		if(CollUtil.isEmpty(rewards)) {
			return Lists.newArrayList();
		}
		player.playerQueue().deleteQueue(deleteQueue);
		player.playerQueue().SetChanged();
		player.sendUserUpdateMsg();
		return ItemBiz.createItemList(rewards);
	}


}
