package com.hm.model.activity.kfworldwarshop;

import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.serverpublic.ServerDataManager;

/**
 * @ClassName KfWorldWarShopActivity
 * @Deacription 跨服世界大战商店
 * @Author zxj
 * @Date 2021/11/26 15:47
 * @Version 1.0
 **/
public class KfWorldWarShopActivity extends AbstractActivity {
    private int serverLv;

    public KfWorldWarShopActivity() {
        super(ActivityType.KfWorldWarShop);
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
