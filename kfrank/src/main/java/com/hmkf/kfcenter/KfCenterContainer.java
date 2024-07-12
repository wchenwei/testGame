package com.hmkf.kfcenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KfCenterContainer {

    private KfCenterData centerData;//中心数据

    public void init() {
        this.centerData = KFDataUtils.getKfCenterData();
        if (this.centerData == null) {
            this.centerData = new KfCenterData();
            this.centerData.setId(KFDataUtils.IdName);
            this.centerData.loadDayMark();
            this.centerData.save();
        }
    }

    public KfCenterData getCenterData() {
        return centerData;
    }


}
