package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import lombok.Data;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家战斗记录奖励
 * @date 2023/8/28 11:17
 */
@Data
public class PlayerRecord extends PlayerDataContext {
    //已经领取过的
    private List<String> idList = Lists.newArrayList();

    public boolean haveId(String id) {
        return CollUtil.contains(this.idList,id);
    }

    public void addId(String id) {
        this.idList.add(id);
        SetChanged();
    }

    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerRecord", this);
    }
}
