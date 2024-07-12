package com.hmkf.gametype;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hmkf.config.KFLevelConstants;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Document(collection = "SnumTime")
public class SnumTime {
    @Id
    private int id;
    private long startTime;
    private long endTime;

    private String sdate;
    private String edate;

    public SnumTime(int id, long startTime, long endTime) {
        super();
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sdate = DateUtil.formatDate(new Date(startTime));
        this.edate = DateUtil.formatDate(new Date(endTime));
    }

    public void save() {
        System.err.println(id + "开始时间:" + DateUtil.formatDate(new Date(this.startTime)));
        System.err.println(id + "结束时间:" + DateUtil.formatDate(new Date(this.endTime)));
        MongoUtils.getLoginMongodDB().save(this);
    }

    public static SnumTime getSnumTime() {
        MongodDB mongo = MongoUtils.getLoginMongodDB();
        Criteria criteria = Criteria.where("startTime").lte(System.currentTimeMillis())
                .and("endTime").gt(System.currentTimeMillis());
        Query query = new Query(criteria);
        return mongo.queryOne(query, SnumTime.class);
    }


    public static void loadFirstSnumTime(int totalNum, String startDate) {
        long startTime = DateUtil.beginOfWeek(DateUtil.parse(startDate)).getTime();
        for (int i = 1; i < totalNum; i++) {
            long endTime = startTime + KFLevelConstants.SWeek * (7 * GameConstants.DAY);
            System.err.println(i + "=" + new DateTime(startTime).toDateStr() + ":" + new DateTime(endTime).toDateStr());
            new SnumTime(i, startTime, endTime).save();
            startTime = endTime;
        }
    }

    public static void reloadSnum(int totalNum, String startDate) {
        //本期还是第3期  6月22结束
        MongoUtils.getLoginMongodDB().dropCollection(SnumTime.class);
        loadFirstSnumTime(300, startDate);
    }

    public static void main(String[] args) {
//        MongoUtils.getLoginMongodDB().dropCollection(SnumTime.class);
        loadFirstSnumTime(200, "20210909");
        System.err.println(getSnumTime());
    }

}
