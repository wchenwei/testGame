package com.hm.model.queue;

import com.hm.libcore.util.date.DateUtil;
import com.hm.action.build.vo.CollectionQueueVO;
import com.hm.config.GameConstants;
import com.hm.enums.QueueState;
import com.hm.enums.QueueType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import org.springframework.data.annotation.Transient;

/**
 * @Description: 队列基类，建筑升级队列，生产队列，科技升级队列的基类
 * @author xjt  
 * @date 2019年12月16日16:09:48
 * @version V1.0
 */
public abstract class AbstractQueue {
	private String id;
	private int buildId;
	private long startTime;//订单产生时间
	private int totalTime;//总共用时
	private long endTime;//订单结束时间
	private QueueState state;
	private QueueType queueType;//队列类型
	private boolean freeFast;//是否使用过VIP免费加速 false为没有使用过，true已经使用过
	private int eventSpeedSecond;//事件加速时间(秒),为0则代表没有事件在该队列上
	private int workerSpeedSecond;//工人已加速时间
	private boolean isHelp;//是否已经请求帮助
	@Transient
	private long lastSecond;
	@Transient
	private transient BasePlayer context;
	private transient int beHelpCount;//被帮助次数
	
	public AbstractQueue() {
		super();
	}
	public AbstractQueue(QueueType type,BasePlayer player) {
		this.id = player.getServerId()+"_"+PrimaryKeyWeb.getPrimaryKey("QueueId",player.getServerId());
		this.queueType = type;
		this.state = QueueState.Run;
		this.context = player;
		this.startTime = System.currentTimeMillis();
	}
	public String getId() {
		return id;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public QueueState getState() {
		return state;
	}
	public void setState(QueueState state) {
		this.state = state;
	}
	public int getBuildId() {
		return buildId;
	}
	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public QueueType getQueueType() {
		return queueType;
	}
	
	public BasePlayer getContext() {
		return context;
	}
	public void setContext(BasePlayer context) {
		this.context = context;
	}
	
	public int getEventSpeedSecond() {
		return eventSpeedSecond;
	}
	public void addEventSpeedSecond(int eventSpeedSecond) {
		this.eventSpeedSecond += eventSpeedSecond;
	}
	
	public boolean isHelp() {
		return isHelp;
	}
	public void setHelp(boolean isHelp) {
		this.isHelp = isHelp;
	}
	public boolean isOver() {
		return state==QueueState.Run&&System.currentTimeMillis()>=this.endTime;
	}
	
	public int getWorkerSpeedSecond() {
		return workerSpeedSecond;
	}
	public void addWorkerSpeedSecond(int workerSpeedSecond) {
		this.workerSpeedSecond += workerSpeedSecond;
	}
	/**
	 * 小于三分钟免费
	 * @return
	 */
	public boolean isCanFreeSpeed() {
		return this.endTime-System.currentTimeMillis()>3*60*GameConstants.SECOND;
	}
	//完成
	public void complete(){
		this.state=QueueState.Complete;
		this.totalTime=0;
		this.endTime=0;
	}
	public boolean isComplete(){
		return state==QueueState.Complete||isOver();
	}
	public void calLastTime() {
		this.lastSecond = DateUtil.getRemainTime(this.endTime);
	}
	/**
	 * 收取队列
	 * @return 
	 */
	public abstract CollectionQueueVO collectionProduct(Player player);
	/**
	 * 队列加速
	 * @param time 加速时间（ms）
	 */
	public void speed(long time) {
		this.endTime = Math.max(this.endTime-=time, System.currentTimeMillis());
	}
	
	public boolean isFreeFast() {
		return freeFast;
	}
	public void setFreeFast(boolean freeFast) {
		this.freeFast = freeFast;
	}
	//是否已经有事件加速在队列身上
	public boolean isEventSpeed() {
		return this.eventSpeedSecond>0;
	}
	public void clearEventSpeed(){
		this.eventSpeedSecond=0;
	}
	public int getBeHelpCount() {
		return beHelpCount;
	}
	public void addBeHelpCount() {
		this.beHelpCount++;
	}
	public long getLastSecond() {
		return lastSecond;
	}
	
	
}
