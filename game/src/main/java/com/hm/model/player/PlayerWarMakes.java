package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.WarMakesConfig;
import com.hm.enums.WarMakesEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description
 *          战令
 * @date 2021/4/10 20:15
 */
@Data
public class PlayerWarMakes extends PlayerDataContext {
    // 赛季活动Id
    private int seasonId;
    // 期数
    private int stage;
    // 战令等级
    private int level;
    // 战令经验
    private int experience;
    // 购买的 战令类型
    private Set<Integer> buyTypeSet = Sets.newHashSet();
    // 已领取的战令   类型 - ids
    private Map<Integer, List<Integer>> receiveMap = Maps.newConcurrentMap();
    // 经验书购买次数
    private int experienceBuyTimes;

    public void addExperience(int experience){
        this.experience += experience;
        WarMakesConfig config = SpringUtil.getBean(WarMakesConfig.class);
        this.level = config.getLevel(this.experience);
        SetChanged();
    }

    public void addBuyType(int type){
        this.buyTypeSet.add(type);
    }

    public void addExperienceBuyTimes(){
        this.experienceBuyTimes += 1;
    }

    public boolean isCanReceive(WarMakesEnum warMakesEnum, int id){
        if(!warMakesEnum.typeIsCanReceive(this)){
            return false;
        }
        return !this.receiveMap.getOrDefault(warMakesEnum.getType(), Lists.newArrayList()).contains(id);
    }

    public void addReceive(int type, int id){
        List<Integer> list = receiveMap.getOrDefault(type, Lists.newArrayList());
        list.add(id);
        this.receiveMap.put(type, list);
        SetChanged();
    }

    public void addAllReceive(int type, List<Integer> ids){
        receiveMap.put(type, ids);
        SetChanged();
    }

    public void checkWarMakes(int seasonId){
        if(this.seasonId == seasonId){
            return;
        }
        WarMakesConfig config = SpringUtil.getBean(WarMakesConfig.class);
        this.seasonId = seasonId;
        this.stage = config.getStage(seasonId);
        this.experience = 0;
        this.buyTypeSet.clear();
        this.receiveMap.clear();
        this.experienceBuyTimes = 0;
        this.level = 0;
        SetChanged();
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("playerWarMakes", this);
    }

}
