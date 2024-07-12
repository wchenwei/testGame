package com.hmkf.model.kfwarrecord;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.model.war.FightDataRecord;
import com.hm.model.war.TempResult;
import com.hm.util.cos.RecordUtils;
import com.hm.war.sg.CLunWarResult;
import com.hm.war.sg.WarResult;
import com.hmkf.db.KfDBUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@NoArgsConstructor
public class KfWarRecord extends DBEntity<String> {
    private TempResult result;
    @Indexed
    private long atkId;
    @Indexed
    private long defId;
    private long winId;
    private long addTime;

    private Object atkInfo;
    private Object defInfo;

    private int type;//0-正常战斗可以复仇  1-已经复仇  2-复仇战斗
    private transient AtkRecord atkRecord;

    public KfWarRecord(CLunWarResult warResult,AtkRecord atkRecord,boolean isRevenge) {
        setId(RecordUtils.getKfId());
        initWarRecord(warResult);
        this.atkInfo = warResult.getAtkInfo();
        this.defInfo = warResult.getDefInfo();
        this.atkRecord = atkRecord;
        this.addTime = System.currentTimeMillis();
        this.type = isRevenge?2:0;
        RecordUtils.saveCosObj(getId(), warResult);
    }

    public void initWarRecord(CLunWarResult warResult) {
        if(CollUtil.isEmpty(warResult.getWarResults())) {
            return;
        }
        List<WarResult> warResults = warResult.getWarResults();
        WarResult result = warResults.get(warResults.size()-1);

        this.atkId = result.getAtk().getPlayerId();
        this.defId = result.getDef().getPlayerId();
        this.result = new TempResult(result);
        this.winId = result.isAtkWin() ? this.atkId : this.defId;
        addWarResult(getId(), result);
    }


    private void addWarResult(String id, WarResult result) {
        if (RecordUtils.saveDB) {
            FightDataRecord fightDataRecord = new FightDataRecord(id, 0, result);
            fightDataRecord.saveDbByMongoDb(KfDBUtils.getMongoDB(), "FightDataRecord");
        }
    }

    //失败的人才可以复仇
    public boolean isCanRevenge(long playerId) {
        if(this.type == 0 && playerId == defId) {
            return this.winId != playerId;
        }
        return false;
    }

    public void save() {
        KfDBUtils.getMongoDB().save(this);
    }

    public static void clearDB() {
        KfDBUtils.getMongoDB().dropCollection(KfWarRecord.class);
    }

}
