package com.hm.servercontainer.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.db.PlayerUtils;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.servercontainer.ItemContainer;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * 玩家数据具体项容器
 * @Author xjt
 * @Date 2022-05-31
 **/
public class PlayerDataItemContainer extends ItemContainer {
    //key:玩家id  value:玩家名字
    @Getter
    private Set<String> nameToPlayerId = Sets.newHashSet();

    private List<String> randomNames = Lists.newArrayList();


    public PlayerDataItemContainer(int serverId) {
        super(serverId);
    }

    @Override
    public void initContainer() {
        MongodDB mongo = MongoUtils.getMongodDB(getServerId());
        this.nameToPlayerId = PlayerUtils.getPlayerNameForStr(mongo);
    }

    /**
     * 检查名字是否重复
     * @param name
     * @return
     */
    public boolean checkNameRepeat(String name) {
        return CollUtil.contains(this.nameToPlayerId, name);
    }

    /**
     * 更新数据缓存
     *
     * @param player
     */
    public void updateName(String oldName, String newName) {
        if (oldName != null) {
            this.nameToPlayerId.remove(oldName);
        }
        this.nameToPlayerId.add(newName);
    }

    public void addRandomName(String name){
        if(randomNames.contains(name)){
            return;
        }
        if(randomNames.size()>100){
            randomNames.remove(0);
        }
        this.randomNames.add(name);
    }
    public boolean isContainsRandom(String name){
        return randomNames.contains(name);
    }
}