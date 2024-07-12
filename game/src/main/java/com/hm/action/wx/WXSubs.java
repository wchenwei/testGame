package com.hm.action.wx;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.model.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description:微信订阅
 * @date 2023/4/23 14:57
 */
@Setter
@Getter
@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class WXSubs extends BaseEntityMapper<Long> {
    private String gameId;
    private String openId;
    //订阅的模板id
    private List<Integer> tmpList = Lists.newArrayList();



    public WXSubs(Player player, String openId) {
        setServerId(player.getServerId());
        setId(player.getId());
        this.openId = openId;
    }

    public void addTempList(List<Integer> tmpList) {
        for (int s : tmpList) {
            if(!CollUtil.contains(this.tmpList,s)) {
                this.tmpList.add(s);
            }
        }
    }

    public void remove(WXSubsType subsType) {
        CollUtil.removeAny(tmpList,subsType.getType());
    }

    public boolean isCanSend(WXSubsType subsType) {
        return CollUtil.contains(this.tmpList,subsType.getType());
    }


    public void saveDB() {
        if(CollUtil.isEmpty(tmpList)) {
            this.delete();
        }else{
            super.saveDB();
        }
    }

    public static WXSubs getWXSubs(int serverId,long playerId) {
        return queryOne(serverId,playerId,WXSubs.class);
    }


}
