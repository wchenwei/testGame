package com.hm.model.activity.kfactivity;

import cn.hutool.core.util.StrUtil;
import com.hm.enums.ActivityType;
import com.hm.handler.GamePlayerMsgForwardProxy;
import com.hm.libcore.mongodb.ServerGroup;
import com.hm.libcore.spring.SpringUtil;
import com.hm.message.ServerMsgType;
import com.hm.model.activity.AbstractActivity;
import com.hm.rpc.KFRpcType;
import com.hm.rpc.RpcManager;
import com.hm.server.GameServerManager;
import lombok.Getter;

@Getter
public abstract class AbstractKfActivity extends AbstractActivity{
	private String serverUrl;//跨服连接的服务器地址
	
	public AbstractKfActivity(ActivityType type) {
		super(type);
	}


	@Override
	public void initServerLoad() {
		checkServerUrl();
	}

	@Override
	public boolean checkCanAdd() {
		ServerGroup serverGroup =  GameServerManager.getInstance().getServerGroup(getServerId());
		if(serverGroup != null) {
			this.serverUrl = buildUrl(serverGroup);
			return checkTime();
		}
		return false;
	}
	
	public String buildUrl(ServerGroup serverGroup) {
		return serverGroup.getServerurl();
	}
	
	public boolean checkTime() {
		return true;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
		checkServerUrl();
	}

	public boolean isOpenForServer() {
		return isOpen() && StrUtil.isNotEmpty(this.serverUrl);
	}

	public void checkServerUrl() {
		if(StrUtil.isEmpty(serverUrl)) {
			return;
		}
		GamePlayerMsgForwardProxy gameProxy = SpringUtil.getBean(GamePlayerMsgForwardProxy.class);
		gameProxy.register(getServerMsgType(),getServerId(),serverUrl);
		RpcManager.startRpc(KFRpcType.KF,serverUrl);
	}
	public ServerMsgType getServerMsgType() {
		return ServerMsgType.KFYZ;
	}

}
