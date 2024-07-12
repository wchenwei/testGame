package com.hm.action.personalchat.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.personalchat.vo.ChatVo;
import com.hm.action.personalchat.vo.FriendErrorInfo;
import com.hm.action.personalchat.vo.FriendVo;
import com.hm.action.personalchat.vo.PlayerFriendVo;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.message.MessageComm;
import com.hm.model.friend.FriendVal;
import com.hm.model.personalchat.FriendChat;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.util.RedisUtil;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ServerUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/3/2 15:44
 */

@Biz
public class FriendBiz implements IObserver {
    @Resource
    private CommValueConfig commValueConfig;

    /**
     * 发送玩家好友列表
     *
     * @param player
     */
    public void sendFriendList(Player player) {
        JsonMsg msg = JsonMsg.create(MessageComm.S2C_Friend_list);
        msg.addProperty("friends", new PlayerFriendVo(player.playerFriend()));
        player.saveDB();
        player.sendMsg(msg);
    }

    //检查好友列表，申请列表中是否有无效果用户
    private void checkFriend(Player player) {
        List<Long> friendIds = Lists.newArrayList(player.playerFriend().getFriendMap().keySet());
        List<Long> requestIds = Lists.newArrayList(player.playerFriend().getRequestMap().keySet());
        List<Long> beRequestIds = Lists.newArrayList(player.playerFriend().getBeRequestMap().keySet());
        friendIds.forEach(t -> {
            if (isCanDel(player,t)) {
                player.playerFriend().delFriend(new Long(t));
            }
        });
        requestIds.forEach(t -> {
            if (isCanDel(player,t)) {
                player.playerFriend().refuse(new Long(t));
            }
        });
        beRequestIds.forEach(t -> {
            if (isCanDel(player,t)) {
                player.playerFriend().deleteRequest(new Long(t));
            }
        });
        if (player.playerFriend().Changed()) {
            player.sendUserUpdateMsg();
        }
    }


    /**
     * 处理玩家添加好友
     *
     * @param player
     * @param friend
     */
    public void addFriend(Player player, Player friend) {
        player.playerFriend().addFriend(friend.getId());
        //好友添加
        JsonMsg msg = JsonMsg.create(MessageComm.S2C_FriendAdd);
        msg.addProperty("newFriend", new FriendVo(friend.getId()));
        player.sendUserUpdateMsg();
        sendFriendList(player);
        player.sendMsg(msg);
    }

    public void delFriend(Player player, long friendId) {
        if (player == null) {
            return;
        }
        player.playerFriend().delFriend(friendId);
        player.sendUserUpdateMsg();
        JsonMsg msg = JsonMsg.create(MessageComm.S2C_FriendDel);
        msg.addProperty("fid", friendId);
        player.sendMsg(msg);
        player.sendUserUpdateMsg();
    }

    public JsonMsg addFriendReply(Player player, Player friendPlayer, boolean agree) {
        JsonMsg msg = JsonMsg.create(MessageComm.S2C_RequestReply);
        if (agree) {
            //添加成为好友
            this.addFriend(player, friendPlayer);
            this.addFriend(friendPlayer, player);
        } else {
            player.playerFriend().deleteRequest(friendPlayer.getId());
            player.sendUserUpdateMsg();
            sendFriendList(player);
            friendPlayer.playerFriend().refuse(player.getId());
            friendPlayer.sendUserUpdateMsg();
            sendFriendList(friendPlayer);
        }
        msg.addProperty("agree", agree);
        msg.addProperty("playerInfo", RedisUtil.getPlayerRedisData(player.getId()));
        return msg;
    }

    public boolean checkFriendNum(Player player, Player friendPlayer) {
        int friendLimit = commValueConfig.getCommValue(CommonValueType.FriendLimit);
        int friendNum = player.playerFriend().getFriendNum();
        if (friendNum >= friendLimit) {
            player.sendErrorMsg(SysConstant.friendLimit);
            return false;
        }
        int otherFriendNum = friendPlayer.playerFriend().getFriendNum();
        if (otherFriendNum >= friendLimit) {
            player.sendErrorMsg(SysConstant.targetFriendLimit);
            return false;
        }
        return true;
    }

    /**
     * @param player
     * @param msg
     * @return void
     * @description 好友之间私聊
     * @author wyp
     * @date 2021/12/14 18:07
     */
    public void chatToFriend(Player player, long fid, int type, String content) {
        FriendVal friendVal = player.playerFriend().getFriendVal(fid);
        if (friendVal == null) {
            return;
        }
        friendVal.setTime(System.currentTimeMillis());
        Player friend = PlayerUtils.getPlayer(fid);
        ChatVo chatVo = new ChatVo(player.getId(), type, content, fid);
        if (friend != null && friend.isOnline()) {
            JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Friend_Chat);
            // 好友在线，直接推送
            retMsg.addProperty("friendChatVo", Lists.newArrayList(chatVo));
            friend.sendMsg(retMsg);
        } else {
            // 存储到 redis 中，登录时 再推过去
            FriendChat friendChatVo = FriendChat.get(player.getServerId(), fid);
            friendChatVo.addMsgAndSave(chatVo);
        }
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_Friend_Chat);
        jsonMsg.addProperty("friendChatVo", Lists.newArrayList(chatVo));
        player.sendMsg(jsonMsg);
        player.saveDB();
    }

    public void loginSendFriendChat(Player player) {
        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Friend_Chat);
        FriendChat friendChatVo = FriendChat.get(player.getServerId(), player.getId());
        if (CollUtil.isEmpty(friendChatVo.getChatVos())) {
            return;
        }
        retMsg.addProperty("friendChatVo", friendChatVo.getChatVos());
        // 移除redis数据
        friendChatVo.del();
        player.sendMsg(retMsg);
    }

    public List<FriendErrorInfo> doRequestReply(Player player, List<Integer> fids, boolean agree) {
        int friendLimit = commValueConfig.getCommValue(CommonValueType.FriendLimit);
        List<FriendErrorInfo> errorInfos = Lists.newArrayList();
        fids.forEach(fid -> {
            if (!player.playerFriend().beRequestMap.containsKey(fid)) {
                return;//不在被请求列表里
            }
            FriendVal friendVal = player.playerFriend().getFriendVal(fid);
            if (friendVal != null) {
                return;//已经是好友了
            }
            Player friendPlayer = PlayerUtils.getPlayer(fid);
            if (friendPlayer == null) {
                return;//还有不存在
            }
            if (agree && player.playerFriend().getFriendNum() >= friendLimit) {
                errorInfos.add(new FriendErrorInfo(fid, SysConstant.friendLimit));
                return;
            }
            if (agree && friendPlayer.playerFriend().getFriendNum() >= friendLimit) {
                errorInfos.add(new FriendErrorInfo(fid, SysConstant.targetFriendLimit));
                return;
            }
            if (agree && player.playerChat().getBlackSet().contains(fid)) {
                errorInfos.add(new FriendErrorInfo(fid, SysConstant.friendBlack));
                return;
            }
            if (agree && friendPlayer.playerChat().getBlackSet().contains(player.getId())) {
                errorInfos.add(new FriendErrorInfo(fid, SysConstant.targetFriendBlack));
                return;
            }
            JsonMsg jsonMsg = addFriendReply(player, friendPlayer, agree);
            //通知申请者
            friendPlayer.sendMsg(jsonMsg);
        });
        return errorInfos;
    }

    //删除请求
    public void delReques(Player player, long id) {
        player.playerFriend().deleteRequest(id);
        player.sendUserUpdateMsg();
        sendFriendList(player);

    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginSuc, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        checkFriend(player);
        loginSendFriendChat(player);
    }

    public boolean isCanDel(Player player,long id){
        if(!RedisUtil.isExitPlayer(id)){
            return true;
        }
        if(ServerUtils.getServerId(id)!=player.getServerId()){
            return true;
        }
        return false;
    }

}
