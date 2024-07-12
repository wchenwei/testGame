package com.hmkf.kfcenter;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.util.TimeUtils;
import lombok.Data;

import java.util.Date;

@Data
public class KfCenterData extends DBEntity<String> {
    private KfCenterLevelInfo centerLevel = new KfCenterLevelInfo();
    private String serverDayMark;

    public void save() {
        KFDataUtils.saveOrUpdate(this);
    }



    public void loadDayMark() {
        String mark = TimeUtils.formatSimpeTime2(new Date());
        if (!StrUtil.equals(mark, this.serverDayMark)) {
            this.serverDayMark = mark;
            System.err.println("服务器标识:" + this.serverDayMark);
        }
    }

}
