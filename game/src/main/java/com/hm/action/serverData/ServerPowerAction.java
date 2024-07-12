package com.hm.action.serverData;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.config.ServerPowerConfig;
import com.hm.enums.PlayerTitleType;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerPresidentPower;
import com.hm.model.serverpublic.TitleData;
import com.hm.observer.ObservableEnum;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class ServerPowerAction extends AbstractPlayerAction {
	@Resource
	private ServerPowerBiz serverPowerBiz;
	@Resource
	private ServerPowerConfig serverPowerConfig;

	//设置中心城
	@MsgMethod(MessageComm.C2S_ServerPower_CenterCitys)
	public void openBoss(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		TitleData titleData = serverData.getTitleData();
		if(player.getId()!=titleData.getTitlePlayer(PlayerTitleType.PRESIDENT.getType())){
			//不是总统，没有权利
			return;
		}
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		if(!serverPowerBiz.isInCenterCitySetTime()){
			return;
		}
		if(serverPresidentPower.haveCenterCity()){
			//上一个中心城市正在生效中
			return;
		}
		List<Integer> cityIds = StringUtil.splitStr2IntegerList(msg.getString("cityIds"), ",");
		if(cityIds.size()<3){
			return;
		}
		//检查城市是否是大城市
		if(!serverPowerBiz.isBigCity(cityIds)){
			return;
		}
		serverPresidentPower.setCenterCity(cityIds);
		player.notifyObservers(ObservableEnum.Power_CenterCity,cityIds);
		serverData.save();
		serverPowerBiz.broadPowerChange(player.getServerId());
		player.sendMsg(MessageComm.S2C_ServerPower_CenterCitys, cityIds);
		
	}
	
	//颁布军事政策
	@MsgMethod(MessageComm.C2S_ServerPower_MilitaryPolicy)
	public void militaryPolicy(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		TitleData titleData = serverData.getTitleData();
		if(player.getId()!=titleData.getTitlePlayer(PlayerTitleType.PRESIDENT.getType())){
			//不是总统，没有权利
			return;
		}
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		if(serverPresidentPower.haveMilitaryPolicy()){
			//上一个军事政策正在生效中
			return;
		}
		long expLimit = serverPowerConfig.getExpLimit(serverData.getServerStatistics().getServerLv());
		serverPresidentPower.promulgateMilitaryPolicy(expLimit);
		serverData.save();
		player.notifyObservers(ObservableEnum.Power_Policy, player.getName());
		serverPowerBiz.broadPowerChange(player.getServerId());
		player.notifyObservers(ObservableEnum.MilitaryPolicy,serverPresidentPower.getMilitaryPolicy().getId());
		player.sendMsg(MessageComm.S2C_ServerPower_MilitaryPolicy, expLimit);
	}
	
	//制裁
	@MsgMethod(MessageComm.C2S_ServerPower_Punish)
	public void Punish(Player player, JsonMsg msg){
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		TitleData titleData = serverData.getTitleData();
		if(player.getId()!=titleData.getTitlePlayer(PlayerTitleType.PRESIDENT.getType())){
			//不是总统，没有权利
			return;
		}
		ServerPresidentPower serverPresidentPower = serverData.getServerPresidentPower();
		if(!serverPresidentPower.isCanPunish()){
			return;
		}
		int id = msg.getInt("id");
		serverPresidentPower.punish(id);
		serverData.save();
		player.notifyObservers(ObservableEnum.Power_Punish,id);
		serverPowerBiz.broadPowerChange(player.getServerId());
		player.sendMsg(MessageComm.S2C_ServerPower_Punish, id);
		player.sendMsg(MessageComm.S2C_ServerPower_PunishOpen,serverPowerBiz.createPunishVo(player.getServerId()));
	}
	//制裁
	@MsgMethod(MessageComm.C2S_ServerPower_PunishOpen)
	public void OpenPunish(Player player, JsonMsg msg){
		player.sendMsg(MessageComm.S2C_ServerPower_PunishOpen,serverPowerBiz.createPunishVo(player.getServerId()));
	}
}
