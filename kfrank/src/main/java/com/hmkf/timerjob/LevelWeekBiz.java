package com.hmkf.timerjob;

import com.google.common.collect.Lists;
import com.hmkf.config.KfRankConfig;
import com.hmkf.kfcenter.CenterBiz;
import com.hmkf.kfcenter.KfCenterData;
import com.hmkf.level.LevelGroupContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class LevelWeekBiz {
    @Resource
    private LevelGroupContainer levelGroupContainer;
    @Resource
    private KfRankConfig kfRankConfig;
    @Resource
    private CenterBiz centerBiz;

    public void doWeek() {
        try {
            KfCenterData centerData = centerBiz.getCenterData();
            centerData.getCenterLevel().setSkilTypeId(kfRankConfig.rankTankSkill());
            centerData.save();
        } catch (Exception e) {
            log.error("week出现异常:", e);
        }
    }



    public static <T> List<T> listSub(List<T> list, int start, int end) {
        start = Math.max(start, 0);
        end = Math.min(end, list.size());
        return Lists.newArrayList(list.subList(start, end));
    }
}
