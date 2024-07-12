package com.hm.action.guildwar.biz;

import com.hm.annotation.Broadcast;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.enums.GuildWarStatus;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.timerjob.GuildWarUtils;
import com.hm.timerjob.WarStateModel;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * 
 * @Description: 战斗奖励
 * @author siyunlong  
 * @date 2018年11月23日 下午12:15:28 
 * @version V1.0
 */
@Biz
public class GuildWarBiz extends NormalBroadcastAdapter {
	@Resource
    private CommValueConfig commValueConfig;


	@Scheduled(cron="0 50 19 * * ?")
	public void doPreWar() {
		for (int serverId : GuildWarUtils.getCanStartWarServerList()) {
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.GuildWarHerald, serverId);
		}
	}

	@Broadcast(ObservableEnum.GuildWarStart)
	public void doGuildWarStart(ObservableEnum observableEnum, Player player, Object... argv) {
		int serverId = (int)argv[0];
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_GuildWarState);
		msg.addProperty("warOpenState", GuildWarUtils.warStateModel);
		PlayerContainer.broadPlayer(serverId,msg);
	}

	@Broadcast(ObservableEnum.GuildWarEnd)
	public void doGuildWarEnd(ObservableEnum observableEnum, Player player, Object... argv) {
		int serverId = (int)argv[0];
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_GuildWarState);
		msg.addProperty("warOpenState", GuildWarUtils.getWarStateModel(serverId));
		PlayerContainer.broadPlayer(serverId,msg);
	}

	@Broadcast(ObservableEnum.PlayerLoginSuc)
	public void doLOGIN(ObservableEnum observableEnum, Player player, Object... argv) {
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_GuildWarState);
		msg.addProperty("warOpenState", buildPlayerWarState(player));
		player.sendMsg(msg);
	}

	public WarStateModel buildPlayerWarState(Player player) {
		WarStateModel warStateModel = GuildWarUtils.getWarStateModel(player.getServerId());
		if(warStateModel.getState() == GuildWarStatus.None.getType()) {
			return warStateModel;
		}
		if(GuildWarUtils.isCanOpenGuildWar(player.getServerId())) {
			return warStateModel;//正常开启
		}
		return warStateModel.tomorrowState();//明日开启
	}

}
