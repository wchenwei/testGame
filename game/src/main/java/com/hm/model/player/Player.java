package com.hm.model.player;

import com.hm.action.troop.vo.WorldTroopVo;
import com.hm.actor.OnceQueueTaskType;
import com.hm.libcore.actor.once.IOnceTask;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.mongoqueue.MongoQueue;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.stream.Collectors;

public class Player extends BasePlayer implements IOnceTask {
	//玩家临时内存信息
	@Transient
	private transient PlayerTemp playerTemp;
	
	public PlayerTemp playerTemp() {
		if(this.playerTemp==null) this.playerTemp=new PlayerTemp();
		this.playerTemp.LateInit(this);
		return this.playerTemp;
	}
	
	public long getCombat() {
		return getPlayerDynamicData().getCombat();
	}
	
	@Override
	public void saveDB() {
		if(isKFPlayer()) {
			return;
		}
		if(!MongoQueue.isClose && (MongoQueue.NoOnlineSaveOpen || isOnline())) {
//			MongoQueue.addWrite(getId());
			OnceQueueTaskType.PlayerDB.putTask(this);
		}else{
			saveNowDB();
		}
	}

	@Override
	public void notifyObservers(ObservableEnum observableEnum, Object... argv) {
		if(isKFPlayer()) {
			return;
		}
		ObserverRouter.getInstance().notifyObservers(observableEnum, this, argv);
	}

	public void sendWorldTroopMessage() {
		List<WorldTroop> worldTroops = TroopServerContainer.of(this).getWorldTroopByPlayer(this);
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_PlayerWorldTroops);
		List<WorldTroopVo> worldTroopVos = worldTroops.stream().map(e -> new WorldTroopVo(this,e)).collect(Collectors.toList());
		msg.addProperty("worldTroops", worldTroopVos);
		sendMsg(msg);
	}

	public boolean isUnlockCity(int cityId) {
		return playerMission().isUnlockCity(cityId);
	}


	@Override
	public void doOnceTask() {
		saveNowDB();
	}


	public boolean isInServerWorld(int serverId) {
		KFPServerUrl kfServer = playerTemp().getKfpServerUrl();
		if(kfServer != null) {
			return kfServer.getServerId() == serverId;
		}else{
			return getServerId() == serverId;
		}
	}

	public boolean isKFPlayer() {
		KFPServerUrl kfpServerUrl = playerTemp().getKfpServerUrl();
		return kfpServerUrl != null && kfpServerUrl.getType() == 1;
	}

}
