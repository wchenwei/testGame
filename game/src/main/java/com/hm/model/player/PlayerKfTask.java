package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.redis.kftask.KFTaskType;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 玩家跨服任务数据
 */
public class PlayerKfTask extends PlayerDataContext {
    private ConcurrentHashMap<Integer, KFTaskItem> taskMap = new ConcurrentHashMap<Integer, KFTaskItem>();

    @Data
    public static class KFTaskItem {
        public List<Integer> idList = Lists.newArrayList();
        public String mark;

        public boolean isContainsId(int id) {
            return CollUtil.contains(idList, id);
        }

        public void addId(int id) {
            this.idList.add(id);
        }
    }

    public KFTaskItem getKFTaskItem(KFTaskType type) {
        return this.taskMap.get(type.getType());
    }

    public void addKFTaskItem(KFTaskType type, KFTaskItem taskItem) {
        this.taskMap.put(type.getType(), taskItem);
        SetChanged();
    }

    public void removeKFTaskItem(int type) {
        if (this.taskMap.containsKey(type)) {
            this.taskMap.remove(type);
            SetChanged();
        }
    }

    public Map<Integer, KFTaskItem> getTaskMap() {
        return taskMap;
    }


    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerKfTask", this);
    }
}
