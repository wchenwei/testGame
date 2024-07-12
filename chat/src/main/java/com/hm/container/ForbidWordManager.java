package com.hm.container;



import com.hm.libcore.util.sensitiveWord.sensitive.BadWordSensitiveUtil;
import com.hm.libcore.util.sensitiveWord.sensitive.ReportSensitiveUtil;
import com.hm.libcore.util.sensitiveWord.sensitive.SysWordSensitiveUtil;
import com.hm.redis.RedisTypeEnum;

import java.util.Set;

public class ForbidWordManager {

    public static void initWordFromDB(){
        // 系统敏感字库
        initSysWord();
        //屏蔽字库
        initBadWord();
        //举报关键词
        initReportWord();
    }

    public static void initSysWord(){
        Set<String> myWordSet = RedisTypeEnum.ForbidKeyWord.getAllKeysAndNotGameMark();
        SysWordSensitiveUtil.getInstance().loadWord(myWordSet);
    }

    public static void initBadWord(){
        Set<String> badWordSet = RedisTypeEnum.BadWord.getAllKeysAndNotGameMark();
        BadWordSensitiveUtil.getInstance().loadWord(badWordSet);
    }

    public static void initReportWord(){
        Set<String> reportWordSet = RedisTypeEnum.ReportBadWord.getAllKeysAndNotGameMark();
        ReportSensitiveUtil.getInstance().loadWord(reportWordSet);
    }
}
