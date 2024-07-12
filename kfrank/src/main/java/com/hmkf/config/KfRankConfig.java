/**
 * Project Name:SLG_GameHot.
 * File Name:MailConfig.java
 * Package Name:com.hm.config.excel
 * Date:2018年4月10日下午3:03:16
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.
 */


package com.hmkf.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.annotation.ConfigLoadIndex;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.enums.KfLevelType;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;
import com.hmkf.config.extra.KfLevelRewardRankTemplate;
import com.hmkf.config.extra.KfLevelRewardVisitTemplate;
import com.hmkf.config.extra.KfLevelSkillTemplate;
import com.hmkf.config.extra.KfPkLevelTemplate;
import com.hmkf.config.template.KfPkScoreBaseTemplate;
import com.hmkf.config.template.NpcArenaExTemplate;

/**
 * @Description: 排行奖励
 * @author siyunlong
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@ConfigLoadIndex(1)//最后加载
@Config("KfRankConfig")
public class KfRankConfig extends ExcleConfig {
    private Map<Integer, KfRankReward> rankMap = Maps.newHashMap();
    private Map<Integer, KfVisitRankReward> visitMap = Maps.newHashMap();
    private KfPkLevelTemplate[] stageCounts;
    private KfPkScoreBaseTemplate[] levelScoreList;

    private Map<Integer, KfLevelSkillTemplate> skillMap;

    private Map<Integer, NpcArenaExTemplate> arenaNpcMap = Maps.newHashMap();
    private ListMultimap<Integer,NpcArenaExTemplate> levelNpcMap = ArrayListMultimap.create();

    @Override
    public void loadConfig() {
//        loadSkillTemplate();
//        loadVisitTemplate();
        loadPkSingleTemplate();
//        loadKfPkScoreBaseTemplate();
        loadNpc();
        loadRankTemplate();
    }


    public void loadNpc() {
        this.arenaNpcMap = json2ImmutableMap(NpcArenaExTemplate::getId,NpcArenaExTemplate.class);
        ListMultimap<Integer,NpcArenaExTemplate> levelNpcMap = ArrayListMultimap.create();
        for (NpcArenaExTemplate value : this.arenaNpcMap.values()) {
            levelNpcMap.put(value.getPk_lv(),value);
        }
        this.levelNpcMap = levelNpcMap;
    }

    private void loadRankTemplate() {
        Map<Integer, KfRankReward> rankMap = Maps.newHashMap();
        List<KfLevelRewardRankTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(KfLevelRewardRankTemplate.class), new TypeReference<ArrayList<KfLevelRewardRankTemplate>>() {
        }));
        for (KfLevelRewardRankTemplate temp : list) {
            temp.init();
            KfRankReward KfRankReward = rankMap.get(temp.getStage());
            if (KfRankReward == null) {
                KfRankReward = new KfRankReward();
                rankMap.put(temp.getStage(), KfRankReward);
            }
            KfRankReward.addRankTemplate(temp);
        }
        this.rankMap = rankMap;
    }

    private void loadVisitTemplate() {
        Map<Integer, KfVisitRankReward> rankMap = Maps.newHashMap();
        List<KfLevelRewardVisitTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(KfLevelRewardVisitTemplate.class), new TypeReference<ArrayList<KfLevelRewardVisitTemplate>>() {
        }));
        for (KfLevelRewardVisitTemplate temp : list) {
            temp.init();
            KfVisitRankReward kfRankReward = rankMap.get(temp.getRank());
            if (kfRankReward == null) {
                kfRankReward = new KfVisitRankReward();
                rankMap.put(temp.getRank(), kfRankReward);
            }
            kfRankReward.addRankTemplate(temp);
        }
        this.visitMap = rankMap;
    }

    private void loadSkillTemplate() {
        List<KfLevelSkillTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(KfLevelSkillTemplate.class), new TypeReference<ArrayList<KfLevelSkillTemplate>>() {
        }));
        Map<Integer, KfLevelSkillTemplate> skillMap = Maps.newConcurrentMap();
        for (KfLevelSkillTemplate kfLevelSkillTemplate : list) {
            kfLevelSkillTemplate.init();
            skillMap.put(kfLevelSkillTemplate.getId(), kfLevelSkillTemplate);
        }
        this.skillMap = skillMap;
    }

    private void loadPkSingleTemplate() {
        List<KfPkLevelTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(KfPkLevelTemplate.class), new TypeReference<ArrayList<KfPkLevelTemplate>>() {
        }));
        int maxtype = list.stream().mapToInt(e -> e.getId()).max().orElse(0);
        KfPkLevelTemplate[] seasonRewards = new KfPkLevelTemplate[maxtype];
        for (KfPkLevelTemplate temp : list) {
            temp.init();
            seasonRewards[temp.getId() - 1] = temp;
        }
        this.stageCounts = seasonRewards;
    }

    private void loadKfPkScoreBaseTemplate() {
        List<KfPkScoreBaseTemplate> levelScoreList = ImmutableList.copyOf(JSONUtil.fromJson(getJson(KfPkScoreBaseTemplate.class), new TypeReference<ArrayList<KfPkScoreBaseTemplate>>() {
        }));
        int maxLv = levelScoreList.stream().mapToInt(e -> e.getLv_up()).max().orElse(120);
        this.levelScoreList = new KfPkScoreBaseTemplate[maxLv];
        for (KfPkScoreBaseTemplate temp : levelScoreList) {
            for (int i = temp.getLv_down(); i <= temp.getLv_up(); i++) {
                this.levelScoreList[i - 1] = temp;
            }
        }
    }


    public KfPkLevelTemplate getStageGroup(int type) {
        return this.stageCounts[type - 1];
    }



    public List<Items> getVisitRewards(int visitRank, int playerLv) {
        KfVisitRankReward KfRankReward = this.visitMap.get(visitRank);
        KfLevelRewardVisitTemplate template = KfRankReward.getRankTemplate(playerLv);
        if (template == null) {
            return Lists.newArrayList();
        }
        return template.getRewardList();
    }

    public int rankTankSkill() {
        try {
            return RandomUtils.randomEle(Lists.newArrayList(this.skillMap.values())).getId();
        } catch (Exception e) {
        }
        return 0;
    }


    public NpcArenaExTemplate getArenaNpc(int id){
        return arenaNpcMap.get(id);
    }

    public List<NpcArenaExTemplate> getNpcIdList(int levelType,int day) {
        return this.levelNpcMap.get(levelType).stream().filter(e -> e.getDay() == day)
                .collect(Collectors.toList());
    }
    public KfRankReward getKfRankReward(int levelType) {
        return this.rankMap.get(levelType);
    }
}






