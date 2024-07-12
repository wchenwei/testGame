package com.hm.model.queue;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.build.vo.CollectionQueueVO;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.templaextra.GuildFactoryPaperExtraTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.QueueState;
import com.hm.enums.QueueType;
import com.hm.log.LogBiz;
import com.hm.model.player.Arms;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.CentreArms;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.centreArms.CentreArmsContainer;

public class ArmsQueue extends AbstractQueue {
	private int index;//位置
	private int paperId;
	public ArmsQueue() {
		super();
	}
	public ArmsQueue(BasePlayer player,int index,int paperId,int totalSecond) {
		super(QueueType.Arms,player);
		this.setState(QueueState.Run);
		this.index = index;
		this.paperId = paperId;
		this.setEndTime(System.currentTimeMillis()+totalSecond*GameConstants.SECOND);
		this.setTotalTime((int)totalSecond);
	}
	
	public int getIndex() {
		return index;
	}
	@Override
	public CollectionQueueVO collectionProduct(Player player) {
		CollectionQueueVO vo = new CollectionQueueVO();
		return vo;
	}
	//完成
	@Override
	public void complete(){
		super.complete();
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		GuildFactoryPaperExtraTemplate tempalte = guildFactoryConfig.getPaper(this.paperId);
		int armsId = tempalte.randomArmsId();//根据parperId随机生成一个武器
		BasePlayer player = super.getContext();
		Arms arms = new Arms(armsId,player.getServerId());
		CentreArmsContainer centreArmsContainer = SpringUtil.getBean(CentreArmsContainer.class);
		CentreArms centreArms = CentreArmsContainer.of(player).getCentreArms(player.getId());
		//判断该类型的武器是否可以装配在此位置
		if(centreArms.isCanUp(index,armsId)){
			arms.setPos(index);
			//装配
			centreArms.up(index,arms);
			//发出信号通知更新玩家属性
			player.notifyObservers(ObservableEnum.ArmsChange);
		}else{
			centreArms.down(index);
		}
		centreArms.save();
		player.playerArms().addArms(arms);
		LogBiz logBiz = SpringUtil.getBean(LogBiz.class);
		logBiz.addGoods(player, armsId, 1, ItemType.Arms.getType(), LogType.ArmsProduce);
		QueueBiz queueBiz = SpringUtil.getBean(QueueBiz.class);
		queueBiz.deleteQueue(this.getContext(),this);
		GuildFactoryBiz guildFactoryBiz = SpringUtil.getBean(GuildFactoryBiz.class);
		guildFactoryBiz.sendArmsPosChange(super.getContext());
		player.sendUserUpdateMsg();
	} 
	
}
