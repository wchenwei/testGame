package com.hm.model.queue;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.build.biz.BuildAutoUpBiz;
import com.hm.action.build.biz.BuildBiz;
import com.hm.action.build.biz.BuildVO;
import com.hm.action.build.vo.CollectionQueueVO;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.config.GameConstants;
import com.hm.enums.QueueType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @Description: 建筑升级队列
 * @author xjt  
 * @date 2019年12月16日16:39:05 
 * @version V1.0
 */
public class BuildUpQueue extends AbstractQueue{
	private int buildQueueType;//所使用的队列类型
	
	public BuildUpQueue() {
		super();
	}
	public BuildUpQueue(BasePlayer player) {
		super(QueueType.BuildUp,player);
	}
	public BuildUpQueue(BasePlayer player,int buildId,long totalTime) {
		super(QueueType.BuildUp,player);
		this.setBuildId(buildId);
		this.setTotalTime((int)totalTime);
		this.setEndTime(System.currentTimeMillis()+totalTime*GameConstants.SECOND);
		//触发观察者事件
        player.notifyObservers(ObservableEnum.BuildLevelUpStart, buildId,this.getId());
	}

	public int getBuildQueueType() {
		return buildQueueType;
	}
	public void setBuildQueueType(int buildQueueType) {
		this.buildQueueType = buildQueueType;
	}
	//完成
	@Override
	public void complete(){
		long lastQueueEndTime = this.getEndTime();//必须写在super.complete()之前
		super.complete();
		QueueBiz queueBiz = SpringUtil.getBean(QueueBiz.class);
		BuildBiz buildBiz = SpringUtil.getBean(BuildBiz.class);
		buildBiz.builLvUp(this.getContext(),this.getBuildId());
		queueBiz.deleteQueue(this.getContext(),this);
		Player player = (Player)super.getContext();
		super.getContext().notifyObservers(ObservableEnum.BuildLvUp, this.getBuildId());
		if(super.getContext().playerBuild().getResCollectTime()==0){
			super.getContext().playerBuild().setResCollectTime(System.currentTimeMillis());
		}
		BuildAutoUpBiz buildAutoUpBiz = SpringUtil.getBean(BuildAutoUpBiz.class);
		//如果可以自动建造
		if(player.playerBuild().inAutoTime(lastQueueEndTime)){
			//找出最应该升级的建筑
			BuildVO buildVo = buildAutoUpBiz.getAutoLevelUp(player);
			if(buildVo!=null){
				//创建自動建造隊列
				BuildUpQueue  autoQueue= buildAutoUpBiz.autoUpBuild(player, buildVo, buildQueueType, lastQueueEndTime);
				if(System.currentTimeMillis()>autoQueue.getEndTime()){//如果该等待队列也已经完成
					autoQueue.complete();
				}
			}
		}
	} 
	@Override
	public CollectionQueueVO collectionProduct(Player player) {
		return null;
	}

	
}
