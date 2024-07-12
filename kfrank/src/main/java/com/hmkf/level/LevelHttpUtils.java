package com.hmkf.level;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.serverConfig.PropertiesConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.gametype.KfGroupContainer;

import cn.hutool.http.HttpUtil;

@Slf4j
public class LevelHttpUtils {
    public static int getPlayerLvNum(List<Integer> serverIds, int minLv) {
        try {
            String url = PropertiesConfig.getHttpServer() + "state/statisticsServer/getPlayerActiveNum";
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("serverIds", StringUtil.list2Str(serverIds, ","));
            paramMap.put("minLv", minLv);
            String result = HttpUtil.post(url, paramMap, 5000);
            return Integer.parseInt(result);
        } catch (Exception e) {
            log.error("获取服务器登记人失败", e);
        }
        return 0;
    }

    /**
     * 查找90级以上的活跃玩家数量
     *
     * @param gameTypeId
     * @return
     */
    public static int getPlayerLvNum(int gameTypeId) {
        KfGroupContainer groupContainer = SpringUtil.getBean(KfGroupContainer.class);
        return getPlayerLvNum(groupContainer.getSeverIds(gameTypeId), KFLevelConstants.MinFBId);
    }

    public static void main(String[] args) {
        System.err.println(getPlayerLvNum(Lists.newArrayList(7, 6, 10, 4, 299), 10));
    }
}
