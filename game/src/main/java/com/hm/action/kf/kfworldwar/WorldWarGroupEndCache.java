package com.hm.action.kf.kfworldwar;

import com.google.common.collect.Maps;
import com.hm.action.kf.kfworldwar.winteam.WinTeamGroupData;
import com.hm.action.kf.kfworldwar.winteam.WinTeamGroupVo;

import java.util.Map;

/**
 * 世界大战分组缓存
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/27 17:08
 */
public class WorldWarGroupEndCache {
    public static Map<String, WinTeamGroupVo> cacheMap = Maps.newConcurrentMap();

    public static WinTeamGroupData getWinTeamGroupData(String id) {
        return WinTeamGroupData.getHashVal(WinTeamGroupData.HashKey, id, WinTeamGroupData.class);
    }

    public static WinTeamGroupVo getWinTeamGroupVo(int groupId, int snum) {
        String id = WinTeamGroupData.buildId(groupId, snum);
        WinTeamGroupVo vo = cacheMap.get(id);
        if (vo != null) {
            return vo;
        }
        WinTeamGroupData winTeamGroupData = getWinTeamGroupData(id);
        if (winTeamGroupData == null) {
            return null;
        }
        vo = new WinTeamGroupVo(winTeamGroupData);
        cacheMap.put(id, vo);
        return vo;
    }
}
