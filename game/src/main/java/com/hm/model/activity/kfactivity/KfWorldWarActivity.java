package com.hm.model.activity.kfactivity;

import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.kf.kfworldwar.WorldWarGroupCache;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarConf;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSnumTime;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSumUtils;
import com.hm.config.GameConstants;
import com.hm.enums.ActivityType;
import com.hm.model.player.BasePlayer;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;

import java.util.Date;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 跨服世界大战
 * @date 2019年4月16日 下午4:14:31
 */
public class KfWorldWarActivity extends AbstractKfActivity {
    public int openType;//是否已经开启 0-未开启  1-等待分配 2-开启
    public long openTime;//开启时间
    public transient int kfwarId;//所在分组id

    private int snum;//赛季信息
    private long snumstartTime;//开始时间
    private long snumcalTime;//赛季结算时间
    private long snumendTime;//赛季结束时间

    @Override
    public void doCreateActivity() {
        int minDay = KfWorldWarConf.MinOpenDay;
        ServerData serverData = ServerDataManager.getIntance().getServerData(getServerId());
        int openDay = serverData.getServerOpenData().getOpenDay();
        //计算当前服务器的倒计时
        int diffDay = Math.max(0, minDay + 1 - openDay);
        this.openTime = DateUtil.beginOfDay(new Date()).getTime() + diffDay * GameConstants.DAY;
        //计算开启时是否处于结算期间,如果是结算期,推迟到下赛季开启
        KfWorldWarSnumTime snumTime = KfWorldWarSumUtils.getCurSnum();
        if (snumTime != null && snumTime.isCalTime(this.openTime)) {
            this.openTime = snumTime.getEndTime();
        }
        this.kfwarId = serverData.getServerKefuData().getKfwwId();
    }

    @Override
    public boolean isCloseForPlayer(BasePlayer player) {
        return this.kfwarId <= 0;
    }

    @Override
    public boolean checkCanAdd() {
        return true;
    }

    @Override
    public boolean isOpenForServer() {
        return openType == 2;
    }

    public KfWorldWarActivity() {
        super(ActivityType.KfWorldWar);
    }

    public void openAct() {
        this.openType = 2;
    }

    public void loadSnumInfo() {
        KfWorldWarSnumTime snumTime = KfWorldWarSumUtils.getCurSnum();
        if (snumTime.getId() != this.snum) {
            this.snum = snumTime.getId();
            this.snumstartTime = snumTime.getStartTime();
            this.snumcalTime = snumTime.getCalTime();
            this.snumendTime = snumTime.getEndTime();
            saveDB();

            ActivityBiz activityBiz = SpringUtil.getBean(ActivityBiz.class);
            activityBiz.broadPlayerActivityUpdate(this);
        }
    }

    @Override
    public void doCheckHourActivity() {
        if (this.kfwarId <= 0) {
            ServerData serverData = ServerDataManager.getIntance().getServerData(getServerId());
            this.kfwarId = serverData.getServerKefuData().getKfwwId();
            saveDB();
        }
        if (isOpenForServer()) {
            loadSnumInfo();
        }
        if (openType > 1 || System.currentTimeMillis() < openTime) {
            return;
        }
        this.openType = 1;
        if (DateUtil.thisHour(true) < 3) {
            return;
        }
        KfWorldWarServerGroup worldWarServerGroup = BaseKfServerGroup.findFitKfServerGroup(getServerId(), KfWorldWarServerGroup.class);
        if (worldWarServerGroup != null) {
            this.openType = 2;
            WorldWarGroupCache.addGroupToCache(worldWarServerGroup);
            loadSnumInfo();
            saveDB();
            SpringUtil.getBean(ActivityBiz.class).broadPlayerActivityUpdate(this);
        }

    }

    public long getSnumstartTime() {
        return this.snumstartTime;
    }
}
