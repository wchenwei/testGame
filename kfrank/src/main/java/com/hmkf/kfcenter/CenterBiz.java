package com.hmkf.kfcenter;

import com.hm.libcore.annotation.Biz;
import com.hmkf.gametype.KfGroupContainer;
import com.hmkf.level.LevelGroupContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@Biz
public class CenterBiz {
    @Resource
    private KfCenterContainer kfCenterContainer;
    @Resource
    private KfGroupContainer groupContainer;
    @Resource
    private LevelGroupContainer levelGroupContainer;


    public void checkLoadOrNewRank() {
        kfCenterContainer.getCenterData().loadDayMark();
        levelGroupContainer.loadData();
    }

    public KfCenterData getCenterData() {
        return kfCenterContainer.getCenterData();
    }

}