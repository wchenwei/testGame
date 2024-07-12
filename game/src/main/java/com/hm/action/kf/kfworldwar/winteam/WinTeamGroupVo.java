package com.hm.action.kf.kfworldwar.winteam;

import com.hm.action.kf.kfworldwar.sum.KfWorldWarConf;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSnumTime;
import com.hm.model.serverpublic.kf.KFPlayerRankData;
import com.hm.redis.ServerNameCache;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 赛季获胜信息
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/6/8 15:45
 */
public class WinTeamGroupVo {
    private int id;//赛季id
    private int winServerId;
    private String winServerName;
    private String sdate;
    private String caldate;
    private String edate;

    private long nextOpenTime;

    private List<KFPlayerRankData> playerList;


    public WinTeamGroupVo(WinTeamGroupData groupData) {
        this.id = groupData.getSnum();
        this.winServerId = groupData.getTeamId();
        this.winServerName = ServerNameCache.getServerName(this.winServerId);

        KfWorldWarSnumTime snumTime = KfWorldWarSnumTime.getKfWorldWarSnumTime(groupData.getSnum());
        this.sdate = snumTime.getSdate();
        this.caldate = snumTime.getCaldate();
        this.edate = snumTime.getEdate();
        this.nextOpenTime = snumTime.getEndTime() + KfWorldWarConf.ReadyTime;
        this.playerList = groupData.getPlayerIds().stream()
                .map(e -> KFPlayerRankData.buildKFPlayerRankData(e)).collect(Collectors.toList());
    }


}
