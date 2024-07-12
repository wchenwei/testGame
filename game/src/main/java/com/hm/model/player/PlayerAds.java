package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.excel.templaextra.AdTemplate;
import lombok.Data;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家广告
 * @date 2023/8/28 11:17
 */
@Data
public class PlayerAds extends PlayerDataContext {
    //玩家广告id  val:= 0-代码看过广告了   1-领取完奖励了
    public Map<Integer,Integer> adMap = Maps.newHashMap();
    //次数
    public Map<Integer,Integer> adCountMap = Maps.newHashMap();
    //广告cd
    private Map<Integer, Long> adCdMap = Maps.newHashMap();

    private int qywxMark;//业务微信的标识


    public void addAd(int id) {
        if(this.adMap.containsKey(id)) {
            return;
        }
        this.adMap.put(id,0);
        SetChanged();
    }
    public boolean rewardAds(int id) {
        if(this.adMap.getOrDefault(id,-1) != 0) {
            return false;
        }
        this.adMap.put(id,1);
        SetChanged();
        return true;
    }

    public boolean haveAds(int id) {
        return this.adMap.containsKey(id);
    }

    public void doDayReset() {
        if(CollUtil.isNotEmpty(this.adMap) || CollUtil.isNotEmpty(this.adCountMap) || CollUtil.isNotEmpty(adCdMap)) {
            this.adMap.clear();
            this.adCountMap.clear();
            this.adCdMap.clear();
            SetChanged();
        }
    }

    public int getTodayCount(int id) {
        return this.adCountMap.getOrDefault(id,0);
    }
    public void addAdCount(int id) {
        this.adMap.remove(id);
        this.adCountMap.put(id,getTodayCount(id)+1);
        SetChanged();
    }

    public void setQywxMark(int qywxMark) {
        this.qywxMark = qywxMark;
        SetChanged();
    }

    public void updateAdCd(int id, long cd) {
        this.adCdMap.put(id,cd);
        SetChanged();
    }


    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerAds", this);
    }
}
