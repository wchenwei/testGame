package com.hmkf.config;

import com.hm.config.excel.CommValueConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.util.StringUtil;
import com.hmkf.model.KfCmType;

public class KFLevelConstants {
    public static final int MaxMatchCount = 1000;
    public static final int MinFBId = 1;
    public static final int SWeek = 4;
    public static int SNum = 1;
    public static final long InitScore = 1000;
    public static final long WeekSHour = 8;
    public static int DayCount = 1;//每日免费次数
    public static int FightItemCard = 60551;

    public static int[] PMS = {1100,100};
    public static int[][] Scores = {
            {15,7},
            {10,5},
            {15,7},
            {20,10},
    };

    public static boolean isNpc(String id) {
        return id.startsWith("npc");
    }

    public static void init() {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        DayCount = commValueConfig.getCommValue(KfCmType.AreanFreeTimes.getType());
        PMS = StringUtil.strToIntArray(commValueConfig.getStrValue(KfCmType.NormalScoresParma.getType()), ",");
        String[] normalInfo = commValueConfig.getStrValue(KfCmType.NormalScores.getType()).split(";");
        int[][] normalScores = new int[normalInfo.length][2];
        for (int i = 0; i < normalInfo.length; i++) {
            normalScores[i] = StringUtil.strToIntArray(normalInfo[i],",");
        }
        Scores = normalScores;
    }

    public static int[] buildScores(long atkScore,long defScore) {
        if(atkScore <= PMS[0]) {
            return Scores[0];
        }
        long diff = atkScore - defScore;
        if(diff > PMS[1]) {
            return Scores[1];
        }else if(diff < -PMS[1]) {
            return Scores[3];
        }
        return Scores[2];
    }
}
