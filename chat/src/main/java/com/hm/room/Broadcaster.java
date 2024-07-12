package com.hm.room;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.Player;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

/**
 * 广播器
 * 所有牵涉到广播消息的实体都可以继承此广播器
 */
@Slf4j
public abstract class Broadcaster {
    //广播组,将消息广播给此组内的所有人
    private ChannelGroup channelGroup;

    public Broadcaster(String groupName) {
        this.channelGroup = new DefaultChannelGroup(groupName, GlobalEventExecutor.INSTANCE);
    }

    public String getName() {
        return channelGroup.name();
    }

    /**
     * 广播消息
     *
     * @param msg 消息体
     */
    public void broadcastMessage(JsonMsg msg) {
        channelGroup.writeAndFlush(msg.createResponse());
    }

    /**
     * 玩家加入此广播器
     *
     * @param player
     */
    public void addGroup(Player player) {
//        try {
//            channelGroup.add(player.getSession().getChannel());
//        } catch (Exception e) {
//            log.error(player.getId() + "加入失败", e);
//        }

    }

    /**
     * 玩家离开此广播器
     *
     * @param player
     */
    public void leaveGroup(Player player) {
//        try {
//            channelGroup.remove(player.getSession().getChannel());
//        } catch (Exception e) {
//            log.error(player.getId() + "离开失败", e);
//        }
    }

    /**
     * 添加广播管理器里的所有对象加入
     *
     * @param other
     */
    public void addBroadcaster(Broadcaster other) {
        //添加通道
        Iterator<Channel> its = other.channelGroup.iterator();
        while (its.hasNext()) {
            this.channelGroup.add(its.next());
        }
    }

    /**
     * 清空此管理器里的所有接受者
     */
    public void clearBroadcaster() {
        this.channelGroup.clear();
    }
}
