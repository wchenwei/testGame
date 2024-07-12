package com.hm.model.activity.kfSeasonShop;

import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.serverpublic.ServerDataManager;

public class KfSeasonShopActivity extends AbstractActivity {
	private int version;
	private int serverLv;
	
	public KfSeasonShopActivity() {
		super(ActivityType.KFSeasonShop);
	}
	@Override
	public void loadExtend(String extend) {
		version = Integer.parseInt(extend);
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getVersion() {
		return version;
	}
	public int getServerLv() {
		return serverLv;
	}
	
	@Override
	public void doCreateActivity() {
		int serverLv = ServerDataManager.getIntance().getServerData(this.getServerId()).getServerStatistics().getServerLv();
		this.serverLv =Math.max(serverLv, 1);
	}
	
}
