package com.hm.servercontainer.personalchat;

import com.google.common.collect.Maps;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.personalchat.PersonalChatRoom;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class PersonalChatItemContainer extends ItemContainer {

    private Map<String, PersonalChatRoom> roomMap = Maps.newConcurrentMap();

    public PersonalChatItemContainer(int serverId) {
        super(serverId);
    }

    @Override
    public void initContainer() {
        try {
            log.info("加载私聊聊天室信息开始:" + this.getServerId());
            getRooms().forEach(t -> {
                this.addRoom(t);
            });
            log.info("加载私聊聊天室信息结束:" + this.getServerId());
        } catch (Exception e) {
            log.error("加载私聊聊天室信息加载出错:" + this.getServerId(), e);
        }
    }

    private List<PersonalChatRoom> getRooms() {
        return RedisMapperUtil.queryListAll(getServerId(), PersonalChatRoom.class);
    }

    public PersonalChatRoom getRoom(String id) {
        return this.roomMap.get(id);
    }

    public void addRoom(PersonalChatRoom room) {
        this.roomMap.put(room.getId(), room);

    }

    public PersonalChatRoom getRoom(long playerId, int targetId) {
        PersonalChatRoom room = this.roomMap.values().stream().filter(t -> t.getPlayerIds().contains(playerId) && t.getPlayerIds().contains(targetId)).findFirst().orElse(null);
        if (room == null) {
            room = new PersonalChatRoom(this.getServerId(), playerId, targetId);
            addRoom(room);
            room.saveDB();
        }
        return room;
    }


}












