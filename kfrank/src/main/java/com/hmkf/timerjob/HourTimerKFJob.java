package com.hmkf.timerjob;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.enums.KfLevelType;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hmkf.action.level.KFLevelEndBiz;
import com.hmkf.action.level.KFLevelRankRewardBiz;
import com.hmkf.action.level.KfTopBiz;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.KfRankReward;
import com.hmkf.config.extra.KfLevelRewardRankTemplate;
import com.hmkf.config.extra.KfPkLevelTemplate;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.db.KFPlayerCacheManager;
import com.hmkf.db.KFPlayerDBCache;
import com.hmkf.db.KfDBUtils;
import com.hmkf.guild.KfGuildUtils;
import com.hmkf.kfcenter.CenterBiz;
import com.hmkf.kfcenter.KfCenterData;
import com.hmkf.level.GameTypeGroup;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.level.rank.RankGroup;
import com.hmkf.model.KFPlayer;
import com.hmkf.model.kfwarrecord.KfWarRecord;
import com.hmkf.redis.KFRankRedisUtils;
import com.hmkf.redis.RankItem;
import com.hmkf.server.ServerGroupManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HourTimerKFJob {
    @Resource
    private KFPlayerContainer playerContainer;
    @Resource
    private CenterBiz centerBiz;
    @Resource
    private KFLevelEndBiz kFLevelEndBiz;
    @Resource
    private KFLevelRankRewardBiz kfLevelRankRewardBiz;

    //每天0点1秒执行
    @Scheduled(cron = "3 0 0 * * ?")
    public void doHourTask() {
        if (!ServerStateCache.serverIsRun()) {
            return;
        }
        KfCenterData centerData = centerBiz.getCenterData();
        ServerGroupManager.getIntance().reloadData();
        //每日加载标识
        centerData.loadDayMark();
        //发放排名奖励
        kfLevelRankRewardBiz.doRankReward();

        if (DateUtil.getCsWeek() == 1) {
            //结算本期数据
            kFLevelEndBiz.doSnumEnd();
        }

        //每日重置
        playerContainer.doDayZero();
        KFNpcContainer.doDayZero();

        KFPlayerCacheManager.getInstance().clearAllCache();
        KFPlayerDBCache.getInstance().clearAllCache();
        KfGuildUtils.clearServerGuild();
    }





}











