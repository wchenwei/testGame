package com.hm.model.queue;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.build.vo.CollectionQueueVO;
import com.hm.action.item.ItemBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.config.GameConstants;
import com.hm.enums.LogType;
import com.hm.enums.QueueState;
import com.hm.enums.QueueType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

public class ProduceQueue extends AbstractQueue {
	private Items item;//生产的物品
	private int index;
	private int num;//选择次数(此次数不一定是产品个数，如配置中一次可生产5个产品，则得到的产品应该是5*num)
	private int plunderNum;//被掠夺数量
	public ProduceQueue() {
		super();
	}
	public ProduceQueue(BasePlayer player,int buildId,int index,int num,Items item,int totalSecond) {
		super(QueueType.Produce,player);
		this.setBuildId(buildId);
		this.setState(QueueState.Run);
		this.index = index;
		this.num = num;
		this.item = item;
		this.setEndTime(System.currentTimeMillis()+totalSecond*GameConstants.SECOND*num);
		this.setTotalTime((int)totalSecond*num);
	}
	
	public int getIndex() {
		return index;
	}

	public int getNum() {
		return num;
	}

	//完成
	@Override
	public void complete(){
		super.complete();
	} 
	
	@Override
	public CollectionQueueVO collectionProduct(Player player) {
		QueueBiz queueBiz = SpringUtil.getBean(QueueBiz.class);
		ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
		CollectionQueueVO vo = new CollectionQueueVO();
		vo.setQueueType(this.getQueueType());
		Items reward = getProduct();
		vo.setItem(reward);
		itemBiz.addItem(player, reward, LogType.Queue_Collection);
		queueBiz.deleteQueue(player,this);
		//增加统计配件信息
		this.getContext().notifyObservers(ObservableEnum.ProduceCollection, reward);
		return vo;
	}
	public Items getProduct() {
		Items reward = item.clone();
		reward.setCount(item.getCount()-plunderNum);
		return reward;
	}
	
	public Items plunder() {
		if(!this.isComplete()) {
			return null;
		}
		this.plunderNum=1;
		Items plunderItem = item.clone();
		plunderItem.setCount(1);
		return plunderItem;
	}

}
