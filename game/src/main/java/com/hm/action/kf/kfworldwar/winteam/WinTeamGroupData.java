package com.hm.action.kf.kfworldwar.winteam;

import com.google.common.collect.Lists;
import com.hm.redis.mode.AbstractRedisHashMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: group的team获胜数据
 * @date 2020年12月9日 上午10:47:26
 */
@NoArgsConstructor
@Data
public class WinTeamGroupData extends AbstractRedisHashMode {
    public static final String HashKey = "kfworldwarwin";
    private int id;//groupId
    private int teamId;//获胜的teamId
    private List<Long> playerIds = Lists.newArrayList();//获胜时的总督id
    private int snum;

    @Override
    public String buildFiledKey() {
        return buildId(id, snum);
    }

    @Override
    public String buildHashKey() {
        return HashKey;
    }


    public static String buildId(int groupId, int snum) {
        return groupId + "_" + snum;
    }
}
