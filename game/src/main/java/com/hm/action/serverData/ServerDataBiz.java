package com.hm.action.serverData;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.config.ServerFunctionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.PlayerContainer;
import com.hm.enums.ServerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerFunction;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

@Biz
public class ServerDataBiz implements IObserver{
	@Resource
	private ServerFunctionConfig serverFunctionConfig;
	@Resource
	private GuildBiz guildBiz;
	
	//广播服务器功能变化信息
	public void broadServerFunctionChange(ServerFunction serverFunction) {
		JsonMsg msg = new JsonMsg(MessageComm.S2C_ServerFunction_Change);
		msg.addProperty("serverFunction",serverFunction);
		PlayerContainer.broadPlayer(serverFunction.getContext().getServerId(), msg);
	}
	//判断服务器功能是否开启
	public boolean isServerFunctionOpen(int serverId,ServerFunctionType serverFunctionType){
		ServerData serverDate = ServerDataManager.getIntance().getServerData(serverId);
		if(serverDate==null){
			return false;
		}
		if(serverDate.getServerFunction().isServerUnlock(serverFunctionType.getType())){
			return true;
		}
		return false;
	}
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildUpdateLv, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		int lv = player.playerLevel().getLv();
		int openCityId = player.playerMission().getOpenCity();
		Guild guild = guildBiz.getGuild(player);
		int guildLv = guild==null?0:guild.guildLevelInfo().getLv();
		serverFunctionConfig.checkFunctionOpen(player.getServerId(),lv,openCityId,guildLv);
	}
	
}
