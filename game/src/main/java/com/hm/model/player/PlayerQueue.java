package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.QueueState;
import com.hm.enums.QueueType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.model.queue.AbstractQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlayerQueue extends PlayerDataContext{
	private ConcurrentHashMap<String, AbstractQueue> queueMap = new ConcurrentHashMap<>();
	private int buyQueueNum;//购买的队列个数

	public void initData(){
		for(AbstractQueue queue:queueMap.values()){
			queue.setContext(super.Context());
		}
	}

	public void addQueue(AbstractQueue queue) {
		queueMap.put(queue.getId(), queue);
		SetChanged();
	} 
	//获得该建筑某种队列
	public List<AbstractQueue>  getQueueByBuildIdAndQueeType(int buildId,QueueType queueType){
		return queueMap.values().stream().filter(t ->t.getBuildId()==buildId&&t.getQueueType()==queueType).collect(Collectors.toList());
	}
	//获得该建筑的所有队列
	public List<AbstractQueue>  getQueueByBuildId(int buildId){
		return queueMap.values().stream().filter(t ->t.getBuildId()==buildId).collect(Collectors.toList());
	}
	/**
	 * 删除队列
	 * @param queue
	 */
	public void deleteQueue(AbstractQueue queue) {
		queueMap.remove(queue.getId());
		SetChanged();
	}
	public List<AbstractQueue> getQueues(){
		return Lists.newArrayList(queueMap.values());
	}
	
	/**
	 * 根据队列类型获取队列数量
	 * @param type
	 * @return
	 */
	public int getQueueNumByType(QueueType type) {
		return (int)queueMap.values().stream().filter(t->t.getQueueType()==type).count();
	}
	
	/**  
	 * 根据类型获取队列  
	 * @param type type
	 * @return  队列
	 *
	 */
	public List<AbstractQueue> getQueueByType(QueueType type) {
		return queueMap.values().stream().filter(t ->t.getQueueType()==type).collect(Collectors.toList());
	}
	//获取某种队列类型某种状态的列表
	public List<AbstractQueue> getQueueByTypeAndState(QueueType type,QueueState queueState) {
		return queueMap.values().stream().filter(t ->t.getQueueType()==type&&t.getState()==queueState).collect(Collectors.toList());
	}

	public <T>T getQueueById(String queueId) {
		if(queueMap.containsKey(queueId)){
			return (T)queueMap.get(queueId);
		}
		return null;
	}
	
	/**  
	 * getQueueByTpyeAndState:(获取指定类型.指定状态下的队列). <br/>  
	 *  
	 * @author zqh  
	 * @param type 队列类型
	 * @param states 状态值
	 * @return  队列容器
	 *
	 */
	public List<AbstractQueue> getQueueByTpyeAndState(QueueType type, QueueState... states) {
		List<AbstractQueue> list = new ArrayList<AbstractQueue>();
		for (AbstractQueue queue : queueMap.values()) {
			if(queue.getQueueType() == type){
				for(QueueState state:states) {
					if(queue.getState() == state) {
						list.add(queue);
					}
				}
			}
		} 
		return list;
	}

	//更新队列
	public void updateQueue(AbstractQueue queue) {
		if(queue != null){
			if(queue.getEndTime()-System.currentTimeMillis()<=0){
				queueComplete(queue.getId());
			}
			SetChanged();
		}
	}
	//队列完成
	public void queueComplete(String queueId){
		//BuildBiz buildBiz = SpringUtil.getBean("BuildBiz");
		AbstractQueue queue = getQueueById(queueId);
		if(queue!=null){
			queue.complete();
			//检查自动建造
			/*if(queue.getQueueType()==QueueType.BuildUp){
				buildBiz.startAutoBuild(super.Context());
			}*/
			SetChanged();
		}
	}
	//添加队列加速事件标识
	public void queueEventSpeed(String queueId,int second){
		AbstractQueue queue = getQueueById(queueId);
		if(queue!=null){
			queue.addEventSpeedSecond(second);
			SetChanged();
		}
	}
	//队列加速 second 秒
	public void queueSpeed(String queueId, int second) {
		AbstractQueue queue = getQueueById(queueId);
		if(queue==null){
			return;
		}
		queue.speed(second*GameConstants.SECOND);
		if(queue.isOver()){
			queue.complete();
		}
		SetChanged();
	}
	
	@Override
	public void fillMsg(JsonMsg msg) {
		for(AbstractQueue queue : queueMap.values()){
			queue.calLastTime();
		}
		msg.addProperty("PlayerQueue", this);
	}

	public <T>T getQueueByBlockIdAndQueueType(int id,
			QueueType type) {
		for (AbstractQueue queue : queueMap.values()) {
			if(queue.getQueueType() == type && queue.getBuildId() == id){
				return (T)queue;
			}
		} 
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(TimeUtils.getTimeString(1523422648770l));
	}
	/**
	 * VIP免费加速
	 * @param queueId
	 * @param time
	 */
	public void freeSpeed(String queueId, int time) {
		AbstractQueue queue = getQueueById(queueId);
		if(queue==null)return;
		queueSpeed(queueId, time);
		queue.setFreeFast(true);
		SetChanged();
	}
	//事件加速
	public void eventSpeed(String queueId) {
		AbstractQueue queue = getQueueById(queueId);
		queueSpeed(queueId, queue.getEventSpeedSecond());
		queue.clearEventSpeed();
		SetChanged();
	}
	//清除所有事件加速
	public void clearEventSpeed() {
		for(AbstractQueue queue:getQueues()){
			if(queue.isEventSpeed()){
				queue.clearEventSpeed();
				SetChanged();
			}
		}
	}
	//发送帮助请求
	public void sendHelp(String queueId) {
		AbstractQueue queue = getQueueById(queueId);
		if(queue==null)return;
		queue.setHelp(true);
		SetChanged();
	}
	
	public int getQueueNum(){
		return 1+buyQueueNum;
	}

	public void buyQueue() {
		this.buyQueueNum += 1;
		SetChanged();
	}

	public int getBuyQueueNum() {
		return buyQueueNum;
	}

	public int getBuyCost() {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		String[] str = commValueConfig.getStrValue(CommonValueType.QueueBuyCost).split(",");
		return Integer.parseInt(str[buyQueueNum]);
	}

	public void deleteQueue(List<String> deleteQueue) {
		if(CollUtil.isEmpty(deleteQueue)) {
			return;
		}
		deleteQueue.forEach(t ->{
			queueMap.remove(t);
		});
		SetChanged();
		
	}
}
