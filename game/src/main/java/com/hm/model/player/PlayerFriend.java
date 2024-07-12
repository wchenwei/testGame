package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.personalchat.vo.FriendGift;
import com.hm.model.friend.FriendVal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerFriend extends PlayerDataContext {
    //我的所有好友
    private Map<Long, FriendVal> friendMap = Maps.newConcurrentMap();
    //我请求别人好友列表
    public Map<Long, Long> requestMap = Maps.newConcurrentMap();
    //别人请求我得好友列表
    public Map<Long, Long> beRequestMap = Maps.newConcurrentMap();

    //礼物map
    public ArrayList<Long> sendGiftList = Lists.newArrayList();
    public ConcurrentHashMap<Long, Integer> getGiftMap = new ConcurrentHashMap();

    public FriendVal getFriendVal(long fid) {
        return friendMap.get(fid);
    }


    public void addFriend(long fid) {
        this.friendMap.put(fid, new FriendVal(System.currentTimeMillis()));
        this.requestMap.remove(fid);
        this.beRequestMap.remove(fid);
        SetChanged();
    }

    public void addRequest(long fid) {
        this.requestMap.put(fid, System.currentTimeMillis());
        SetChanged();
    }

    public void addBeRequest(long fid) {
        this.beRequestMap.put(fid, System.currentTimeMillis());
        SetChanged();
    }

    public void delFriend(long fid) {
        this.friendMap.remove(fid);
        SetChanged();
    }

    public void deleteRequest(long fid) {
        this.beRequestMap.remove(fid);
        SetChanged();
    }

    public void refuse(long fid) {
        this.requestMap.remove(fid);
        SetChanged();
    }

    public int getFriendNum() {
        return this.friendMap.size();
    }


    public void addSendGift(long fid) {
        this.sendGiftList.add(fid);
        SetChanged();
    }
    public void addGetGift(long fid) {
        this.getGiftMap.put(fid,1);
        SetChanged();
    }


    public Map<Long, FriendVal> getFriendMap() {
        return friendMap;
    }

    public Map<Long, Long> getRequestMap() {
        return requestMap;
    }

    public Map<Long, Long> getBeRequestMap() {
        return beRequestMap;
    }

    public void doDayReset() {
        if(CollUtil.isNotEmpty(sendGiftList) || CollUtil.isNotEmpty(getGiftMap)) {
            this.sendGiftList.clear();
            this.getGiftMap.clear();
            SetChanged();
        }
    }

    //从redis
    public void doLoginCheck() {
        List<Long> friendList = FriendGift.getGiftPlayerList(super.Context());
        if(CollUtil.isEmpty(friendList)) {
            return;
        }
        for (long fid : friendList) {
            addGetGift(fid);
        }
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerFriend", this);
    }

    public boolean isMyFriend(long targetId) {
        return this.friendMap.containsKey(targetId);
    }
}
