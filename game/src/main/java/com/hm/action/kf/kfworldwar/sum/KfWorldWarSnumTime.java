package com.hm.action.kf.kfworldwar.sum;

import cn.hutool.core.date.DateTime;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

@NoArgsConstructor
@Data
@Document(collection = "WorldWarSnumTime")
public class KfWorldWarSnumTime {
    @Id
    private int id;
    private long startTime;
    private long calTime;//结算时间
    private long endTime;//结束时间

    private String sdate;
    private String caldate;
    private String edate;

    public KfWorldWarSnumTime(int id, long startTime, long endTime) {
        super();
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sdate = DateUtil.formatDate(new Date(startTime));
        this.edate = DateUtil.formatDate(new Date(endTime));
        this.calTime = endTime - 3 * GameConstants.DAY;
        this.caldate = DateUtil.formatDate(new Date(calTime));
    }

    public void save() {
        System.err.println(id + "开始时间:" + DateUtil.formatDate(new Date(this.startTime)));
        System.err.println(id + "结束时间:" + DateUtil.formatDate(new Date(this.endTime)));
        MongoUtils.getLoginMongodDB().save(this);
    }

    public boolean isFitTime() {
        long now = System.currentTimeMillis();
        return now >= this.startTime && now < endTime;
    }

    public boolean isCalTime(long time) {
        return time >= this.calTime && time < endTime;
    }

    public boolean isReadyTime() {
        return System.currentTimeMillis() - this.startTime < KfWorldWarConf.ReadyTime;
    }

    public boolean isFightWarTime() {
        return isFightWarTime(System.currentTimeMillis());
    }

    public boolean isFightWarTime(long checkTime) {
        return checkTime >= this.startTime && checkTime < calTime;
    }

    public static KfWorldWarSnumTime getSnumTime() {
        MongodDB mongo = MongoUtils.getLoginMongodDB();
        Criteria criteria = Criteria.where("startTime").lte(System.currentTimeMillis())
                .and("endTime").gt(System.currentTimeMillis());
        Query query = new Query(criteria);
        return mongo.queryOne(query, KfWorldWarSnumTime.class);
    }

    public static KfWorldWarSnumTime getKfWorldWarSnumTime(int id) {
        MongodDB mongo = MongoUtils.getLoginMongodDB();
        return mongo.get(id, KfWorldWarSnumTime.class);
    }

    public static void loadFirstSnumTime(int totalNum, String startDate) {
        long startTime = DateUtil.parse(startDate).getTime();
        for (int i = 1; i < totalNum; i++) {
            long endTime = DateUtil.offsetMonth(new Date(startTime), KfWorldWarConf.SnumMonth).getTime();
            if (i == 1) {
                endTime = DateUtil.offsetMonth(DateUtil.beginOfMonth(new Date(startTime)), KfWorldWarConf.SnumMonth).getTime();
            }
            System.err.println("i=" + new DateTime(startTime).toDateStr() + ":" + new DateTime(endTime).toDateStr());
            new KfWorldWarSnumTime(i, startTime, endTime).save();
            startTime = endTime;
        }
    }

    public static void reloadSnum(int totalNum, String startDate) {
        //本期还是第3期  6月22结束
        MongoUtils.getLoginMongodDB().dropCollection(KfWorldWarSnumTime.class);
        loadFirstSnumTime(300, startDate);
    }

    public static void main(String[] args) {
        MongoUtils.getLoginMongodDB().dropCollection(KfWorldWarSnumTime.class);
        loadFirstSnumTime(200, "20210712");
        System.err.println(getSnumTime());
    }

}
