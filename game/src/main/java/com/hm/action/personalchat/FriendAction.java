package com.hm.action.personalchat;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.http.biz.HttpBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.personalchat.biz.FriendBiz;
import com.hm.action.personalchat.vo.FriendErrorInfo;
import com.hm.action.personalchat.vo.FriendGift;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.libcore.util.sensitiveWord.sensitive.BadWordSensitiveUtil;
import com.hm.libcore.util.sensitiveWord.sensitive.SysWordSensitiveUtil;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.friend.FriendVal;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.rmi.LogServerRmiHandler;
import com.hm.sysConstant.SysConstant;
import com.hm.util.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Action
public class FriendAction extends AbstractPlayerAction {
    @Resource
    private FriendBiz friendBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private LogBiz logBiz;
    @Resource
    private HttpBiz httpBiz;
    @Resource
    private GiftPackageConfig giftPackageConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private DropConfig dropConfig;



    //请求添加好友
    @MsgMethod(MessageComm.C2S_RequestAddFriend)
    public void requestAddFriend(Player player, JsonMsg msg) {
        int id = msg.getInt("fid");
        FriendVal friendVal = player.playerFriend().getFriendVal(id);
        if (friendVal != null) {
            return;//已经是好友了
        }
        if(id==player.getId()){
           return;
        }
        if(ServerUtils.getServerId(id)!=player.getServerId()){
            player.sendErrorMsg(SysConstant.Friend_Not_Server);
            return;
        }
        Player requestPlayer = PlayerUtils.getPlayer(id);
        if (requestPlayer == null) {
            return;
        }
        if (!friendBiz.checkFriendNum(player, requestPlayer)) {
            return;// 好友上限
        }
        player.playerFriend().addRequest(id);
        player.sendUserUpdateMsg();
        friendBiz.sendFriendList(player);
        player.sendMsg(MessageComm.S2C_RequestAddFriend);
        if (CollUtil.contains(requestPlayer.playerChat().getBlackSet(), player.getId())) {
            return;
        }

        //发给被请求者
        requestPlayer.playerFriend().addBeRequest(player.getId());
        requestPlayer.sendUserUpdateMsg();
        friendBiz.sendFriendList(requestPlayer);
        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_RequestBeFriend);
        retMsg.addProperty("fid", player.getId());
        requestPlayer.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_RequestReplyBatch)
    public void doRequestReplyBatch(Player player, JsonMsg msg) {
        List<Integer> fids = StringUtil.splitStr2IntegerList(msg.getString("fids"), ",");
        boolean agree = msg.getBoolean("agree");
        List<FriendErrorInfo> errorInfos = friendBiz.doRequestReply(player, fids, agree);
        player.sendMsg(MessageComm.S2C_RequestReplyBatch, errorInfos);
    }

    @MsgMethod(MessageComm.C2S_RequestReply)
    public void doRequestReply(Player player, JsonMsg msg) {
        long fid = msg.getLong("fid");
        boolean agree = msg.getBoolean("agree");
        if (!player.playerFriend().beRequestMap.containsKey(fid)) {
            //删除申请
            friendBiz.delReques(player, fid);
            return;//不在被请求列表里
        }
        FriendVal friendVal = player.playerFriend().getFriendVal(fid);
        if (friendVal != null) {
            //删除申请
            friendBiz.delReques(player, fid);
            return;//已经是好友了
        }
        int friendLimit = commValueConfig.getCommValue(CommonValueType.FriendLimit);
        Player friendPlayer = PlayerUtils.getPlayer(fid);
        if (friendPlayer == null) {
            //删除申请
            friendBiz.delReques(player, fid);
            return;//还有不存在
        }
        if (agree && player.playerFriend().getFriendNum() >= friendLimit) {
            //不删除申请
            player.sendErrorMsg(SysConstant.friendLimit);
            return;
        }
        if (agree && friendPlayer.playerFriend().getFriendNum() >= friendLimit) {
            //不删除申请
            player.sendErrorMsg(SysConstant.targetFriendLimit);
            return;
        }
        if (agree && player.playerChat().getBlackSet().contains(fid)) {
            //删除申请
            friendBiz.delReques(player, fid);
            player.sendErrorMsg(SysConstant.friendBlack);
            return;
        }
        if (agree && friendPlayer.playerChat().getBlackSet().contains(player.getId())) {
            //删除申请
            friendBiz.delReques(player, fid);
            player.sendErrorMsg(SysConstant.targetFriendBlack);
            return;
        }
        JsonMsg jsonMsg = friendBiz.addFriendReply(player, friendPlayer, agree);
        //通知申请者
        friendPlayer.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_FriendDel)
    public void doFriendDel(Player player, JsonMsg msg) {
        long fid = msg.getLong("fid");
        FriendVal friendVal = player.playerFriend().getFriendVal(fid);
        if (friendVal == null) {
            return;//不是好友
        }
        friendBiz.delFriend(player, fid);
        friendBiz.sendFriendList(player);
        Player friendPlayer = PlayerUtils.getPlayer(fid);
        friendBiz.delFriend(friendPlayer, player.getId());
    }

//    @MsgMethod(MessageComm.C2S_FindFriend)
//    public void search(Player player, JsonMsg msg) {
//        String name = msg.getString("name");
//        List<Integer> ids = PlayerUtils.getPlayerIdByName(player.getServerId(), name);
//        List<FriendVo> vos = ids.stream().map(t -> new FriendVo(t)).collect(Collectors.toList());
//        player.sendMsg(MessageComm.S2C_FindFriend, vos);
//    }

    @MsgMethod(MessageComm.C2S_Friend_Chat)
    public void sendPersonalChatNew(Player player, JsonMsg msg) {
        long fid = msg.getLong("fid");
        String content = msg.getString("content");
        int type = msg.getInt("type");
        if (fid == player.getId()) {
            return;
        }
        if (PlayerContainer.isNoChat(player.getId())) {
            player.sendErrorMsg(SysConstant.NoChat);
            return;
        }
        Player target = PlayerUtils.getPlayer(fid);
        if (target == null) {
            return;
        }
        if (!player.playerFriend().isMyFriend(fid)) {
            return;
        }
        if (target.playerChat().getBlackSet().contains(player.getId())) {
            //被对方拉黑，不可以发送
            return;
        }
        if (type == 0) {
            if (player.playerLevel().getLv() < 50) {
                if (!ChatLegalUtil.checkSign(msg)) {
                    log.error(player.getId() + "sign校验未通过关小黑屋----" + content);
                    PlayerContainer.addNoChat(player.getId());
                    httpBiz.blackHome(player.getId(), 2);
                    return;
                }
//                long time = msg.getLong("time");
//                long now = msg.getLong("now");
//                if (!ChatLegalUtil.checkInputLegal(content, time, now)) {
//                    log.error(player.getId() + "输入校验未通过关小黑屋----" + content + "" +
//                            "" + time);
//                    PlayerContainer.addNoChat(player.getId());
//                    httpBiz.blackHome(player.getId(), 2);
//                    return;
//                }
            }
            content = SysWordSensitiveUtil.getInstance().replaceSensitiveWord(content, "*");
            logBiz.addPlayerPersonChatLog(player, fid, content);
        }
        friendBiz.chatToFriend(player, fid, type, content);
        if (content.length() < 5 || player.playerLevel().getLv() > 50) {
            return;
        }
        Set<String> badWords = BadWordSensitiveUtil.getInstance().getSensitiveWord(content);
        if (badWords.size()>0) {
            log.error(player.getId()+":" +content+ "，触发关键词关进小黑屋----" + badWords);
            //关小黑屋
            PlayerContainer.addNoChat(player.getId());
            httpBiz.blackHome(player.getId(), 2);

        }
    }

    @MsgMethod(MessageComm.C2S_Friend_list)
    public void getFriend(Player player, JsonMsg msg) {
        friendBiz.sendFriendList(player);
    }


    @MsgMethod(MessageComm.C2S_FriendGift)
    public void friendGift(Player player, JsonMsg msg) {
        List<Long> fidList = StringUtil.splitStr2LongList(msg.getString("fids"),",");
        if(CollUtil.isEmpty(fidList)){
            fidList = Lists.newArrayList(player.playerFriend().getFriendMap().keySet());
        }

        List<Long> sendList = player.playerFriend().getSendGiftList();
        for (long fid : fidList) {
            if (fid == player.getId() || CollUtil.contains(sendList,fid)
                || !player.playerFriend().isMyFriend(fid)) {
                continue;
            }
            if(sendFriendGift(player,fid)) {
                player.playerFriend().addSendGift(fid);
            }
        }
        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_FriendGift);
        retMsg.addProperty("fids",msg.getString("fids"));
        player.sendMsg(retMsg);
        player.sendUserUpdateMsg();
    }

    @MsgMethod(MessageComm.C2S_FriendGiftGet)
    public void friendGiftReward(Player player, JsonMsg msg) {
        Map<Long, Integer> getGiftMap = player.playerFriend().getGetGiftMap();
        String fidStr = msg.getString("fids");
        List<Long> fidList = StrUtil.isEmpty(fidStr) ?
                Lists.newArrayList(getGiftMap.keySet()):StringUtil.splitStr2LongList(fidStr,",");

        int dropId = commValueConfig.getCommValue(CommonValueType.FriendGiftDrop);

        List<Items> itemsList = Lists.newArrayList();
        for (long fid : fidList) {
            int giftId = getGiftMap.getOrDefault(fid,0);
            if(giftId > 0) {
                List<Items> items = dropConfig.randomItem(dropId);
                if(CollUtil.isNotEmpty(items)) {
                    itemsList.addAll(items);
                }
                getGiftMap.put(fid,0);
                player.playerFriend().SetChanged();
            }
        }
        itemsList = ItemUtils.mergeItemList(itemsList);
        itemBiz.addItem(player,itemsList, LogType.FriendGift);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_FriendGiftGet);
        retMsg.addProperty("fids",fidStr);
        retMsg.addProperty("itemList",itemsList);
        player.sendMsg(retMsg);

        player.sendUserUpdateMsg();
    }

    public static boolean sendFriendGift(Player sendPlayer,long playerId) {
        Player target = PlayerUtils.getOnlinePlayer(playerId);
        if(target != null) {
            //玩家在线直接加
            target.playerFriend().addGetGift(sendPlayer.getId());
            target.sendUserUpdateMsg();
        }else{
            //放入redis
            FriendGift.addGift(playerId,sendPlayer);
        }
        return true;
    }

}
