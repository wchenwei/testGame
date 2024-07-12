package com.hmkf.level;

import java.util.List;
import java.util.Map;

import com.hm.libcore.spring.SpringUtil;
import com.hmkf.action.npc.KFNpcBiz;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.level.rank.RankGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.KfLevelType;
import com.hmkf.kfcenter.KFDataUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "GameTypeGroup")
public class GameTypeGroup {
    @Id
    private int id;
    //段位
    private Map<Integer, List<RankGroup>> groupMap = Maps.newConcurrentMap();
    
    public void init() {
        for (KfLevelType levelType : KfLevelType.values()) {
            List<RankGroup> groupList = groupMap.get(levelType.getType());
            if(groupList == null) {
                groupMap.put(levelType.getType(),Lists.newArrayList());
            }else{
                //检查人数
                for (RankGroup rankGroup : groupList) {
                    rankGroup.checkPlayerCount();
                }
            }
        }
    }

    public RankGroup getRankGroup(int levelType) {
        List<RankGroup> groupList = groupMap.get(levelType);
        if(groupList.isEmpty()) {
            return createNewGroup(1,levelType);
        }
        RankGroup newGroup = groupList.get(groupList.size()-1);
        if(newGroup.getPlayerCount() < KFLevelConstants.MaxMatchCount) {
            return newGroup;
        }
        return createNewGroup(newGroup.getId()+1,newGroup.getLevelType());
    }

    //新建分组
    public RankGroup createNewGroup(int id,int levelType) {
        List<RankGroup> groupList = groupMap.get(levelType);

        RankGroup rankGroup = new RankGroup();
        rankGroup.setId(id);
        rankGroup.setLevelType(levelType);
        rankGroup.buildRankId(this.id);
        groupList.add(rankGroup);
        saveDB();

        System.out.println("new RankGroup:"+rankGroup.getRankId());
        //添加npc
        SpringUtil.getBean(KFNpcBiz.class).initRankGroupNpc(rankGroup);

        return rankGroup;
    }


    public void saveDB() {
        KFDataUtils.getMongoDB().save(this);
    }

    public static GameTypeGroup getGameTypeGroup(int id) {
        return KFDataUtils.getMongoDB().get(id, GameTypeGroup.class);
    }

    public void weekClear() {
        for (KfLevelType levelType : KfLevelType.values()) {
            this.groupMap.put(levelType.getType(), Lists.newArrayList());
        }
        saveDB();
    }
}
