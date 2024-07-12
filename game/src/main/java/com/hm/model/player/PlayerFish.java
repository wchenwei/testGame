package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.model.fish.FishRecord;
import com.hm.model.fish.FishVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.Map;

/**
 * @introduce:  钓鱼玩法
 * @author: wyp
 * @DATE: 2023/10/31
 **/
@Getter
@Setter
public class PlayerFish extends PlayerDataContext{
    // 等级
    private int lv = 1;
    // 经验值
    private int exp;
    // 获取的鱼记录
    private Map<Integer, FishRecord> map = Maps.newHashMap();
    // 连击次数
    private int continuousPerfect;
    // 双倍积分次数
    private int doubleScore;
    // 上次分享时间点
    private long lastShareTime;
    // 本次 钓鱼出的鱼数据
    @Transient
    private transient Map<Integer, Integer> fishPondMap = Maps.newHashMap();

    public void updateRecord(FishVO fishVO){
        if(!fishVO.isFish()){
            return;
        }
        FishRecord fishRecord = this.getFishRecord(fishVO.getId());
        if(fishRecord == null){
            fishRecord = new FishRecord(fishVO.getId());
        }
        fishRecord.updateSize(fishVO);
        map.put(fishVO.getId(), fishRecord);
    }

    // 连击
    public void updateRecordAndPerfect(FishVO fishVO, boolean perfect){
        this.updateRecord(fishVO);
        if(fishVO.isDouble() || !perfect){
            // 双倍期间不算连击
            return;
        }
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        this.continuousPerfect++;
        if(continuousPerfect >= commValueConfig.getCommValue(CommonValueType.FishContinuousPerfect)){
            this.doubleScore = commValueConfig.getCommValue(CommonValueType.FishDoubleScore);
            this.clearContinuousPerfect();
        }
    }

    public void clearContinuousPerfect(){
        this.continuousPerfect = 0;
    }

    public int addExp(int exp){
        this.exp += exp;
        return this.exp;
    }

    public boolean isDouble(){
        if(doubleScore > 0){
            this.doubleScore --;
            return true;
        }
        return false;
    }

    public FishRecord getFishRecord(int id){
        return map.get(id);
    }

    public boolean isFirstRecord(int id){
        return !map.containsKey(id);
    }

    public boolean checkCanShare(int intervalHour){
        return System.currentTimeMillis() >= this.lastShareTime + intervalHour * GameConstants.HOUR;
    }

    public void updateFishPre(int fishPondId, int fish){
        this.fishPondMap.put(fishPondId, fish);
    }

    public int getFishByPondId(int fishPondId){
        return fishPondMap.getOrDefault(fishPondId, 0);
    }

    public void clearFishPondMap(int fishPondId){
        fishPondMap.remove(fishPondId);
        SetChanged();
    }


    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerFish", this);
    }
}
