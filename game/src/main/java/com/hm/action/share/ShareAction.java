package com.hm.action.share;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.LogType;
import com.hm.enums.ShareEnum;
import com.hm.enums.StatisticsType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.redis.RedisKeyUtils;
import com.hm.redis.type.RedisConstants;
import com.hm.redis.util.RedisDbUtil;
import com.hm.sysConstant.SysConstant;
import com.hm.util.PubFunc;
import org.apache.commons.lang.StringUtils;
import com.hm.libcore.annotation.Action;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author wyp
 * @description
 *          分享收藏奖励
 * @date 2021/1/9 14:42
 */
@Action
public class ShareAction extends AbstractPlayerAction {
    // Redis 用户分享数量 前缀
    public static final String prefix = "inviter_count_{}";

    @Resource
    private ItemBiz itemBiz;
    @Resource
    private CommValueConfig commValueConfig;


    @MsgMethod(MessageComm.C2S_SHARE_REWATD)
    public void shareReward(Player player, JsonMsg msg) {
        int type = msg.getInt("type"); // 1001
        StatisticsType statisticsType = StatisticsType.getByType(type);
        if(Objects.isNull(statisticsType)){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        ShareEnum shareEnum = ShareEnum.getShareByType(type);
        if(Objects.isNull(shareEnum)){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        if(!shareEnum.checkCanRevice(player, statisticsType)){
            player.sendErrorMsg(SysConstant.Activity_HaveReward);
            return;
        }
        shareEnum.addReward(player, statisticsType);
        List<Items> list = shareEnum.getItem();
        if(!list.isEmpty()) {
            LogType logType = LogType.SHARE_REWARD.value(shareEnum.getType());
            itemBiz.addItem(player, list, logType);
        }
        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_SHARE_REWATD);
        serverMsg.addProperty("itemList", list);
        serverMsg.addProperty("type",type);
        player.sendMsg(serverMsg);
        player.sendUserUpdateMsg();
    }

    @MsgMethod(MessageComm.C2S_SHARE_REWATD_MAIL)
    public void shareMailReward(Player player, JsonMsg msg) {
        int type = msg.getInt("type");
        ShareEnum shareEnum = ShareEnum.getShareByType(type);
        if(Objects.isNull(shareEnum)){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        StatisticsType statisticsType = StatisticsType.getByType(type);
        if(Objects.isNull(statisticsType)){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        if(!shareEnum.checkCanRevice(player, statisticsType)){
            player.sendErrorMsg(SysConstant.Activity_HaveReward);
            return;
        }
        shareEnum.addReward(player, statisticsType);
        // 发送邮件
        shareEnum.sendMail(player);
        player.sendUserUpdateMsg();
    }


    @MsgMethod(MessageComm.C2S_SHARE_COUNT_ADD)
    public void shareCountAdd(Player player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        if(playerId == 0 || player.getId() == playerId){
            return;
        }
        this.operatorRedisShare(playerId,1);
    }

    @MsgMethod(MessageComm.C2S_SHARE_COUNT)
    public void shareCount(Player player, JsonMsg msg) {
        int redisShare = this.getRedisShare(player);
        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_SHARE_COUNT);
        serverMsg.addProperty("shareCount", redisShare);
        player.sendMsg(serverMsg);
    }
    //分享邀请到微信，分享后前端发送消息就算成功分享
    @MsgMethod(MessageComm.C2S_SHARE_WX)
    public void shareWX(Player player, JsonMsg msg) {
        int type = msg.getInt("type");
        player.notifyObservers(ObservableEnum.ShareWx,type);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_SHARE_WX);
    }


//    @MsgMethod(MessageComm.C2S_SHARE_DRAW)
//    public void drawReward(Player player, JsonMsg msg) {
//        int id = msg.getInt("id");
//        ShareGiftActivity activity = (ShareGiftActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.ShareGift);
//        if(activity == null || activity.isCloseForPlayer(player)) {
//            player.sendErrorMsg(SysConstant.Activity_Close);
//            return;
//        }
//        ShareGiftValue value = (ShareGiftValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.ShareGift);
//
//        List<Integer> drawIds = value.getDrawIds();
//        if(!shareGiftConfig.checkCanReceive(drawIds) || !value.checkCanReward(player, id)){
//            player.sendErrorMsg(SysConstant.Activity_HaveReward);
//            return;
//        }
//        // 翻牌需要的金砖消耗数量
//        int goldNum = 0;
//        // 免费的翻牌次数
//        int redisShare = this.getRedisShare(player);
//        boolean isUsedGold = false;
//        if(redisShare == 0){
//            goldNum = commValueConfig.getDrawUsedGold(value.getUsedGoldNum());
//            isUsedGold = true;
//        }else {
//            this.operatorRedisShare(player.getId(), -1);
//        }
//        // 翻牌奖励对象
//        WechatShareGiftTemplateImpl reward = activity.getReward(player, drawIds);
//        if(Objects.isNull(reward)){
//            player.sendErrorMsg(SysConstant.Activity_HaveReward);
//            return;
//        }
//        Items item = new Items(PlayerAssetEnum.SysGold.getTypeId(), goldNum, ItemType.CURRENCY.getType());
//        if(!itemBiz.checkItemEnoughAndSpend(player, item, LogType.SHARE_REWARD.value(activity.getType()))){
//            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
//            return;
//        }
//        List<Items> rewardList = reward.getRewardList();
//        if(CollectionUtils.isNotEmpty(rewardList)) {
//            itemBiz.addItem(player, rewardList, LogType.SHARE_REWARD.value(activity.getType()));
//        }
//        value.setReward(player, reward.getId(), id, isUsedGold);
//        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_SHARE_DRAW);
//        serverMsg.addProperty("itemList", rewardList);
//        player.sendMsg(serverMsg);
//        player.sendUserUpdateMsg();
//    }



//    @MsgMethod(MessageComm.C2S_LUCKY_DRAW)
//    public void shareLuckyDraw(Player player, JsonMsg msg) {
//        int type = msg.getInt("type");  // 0 免费，1 付费
//        LuckyDrawActivity activity = (LuckyDrawActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.luckyDraw);
//        if(activity == null || activity.isCloseForPlayer(player)) {
//            player.sendErrorMsg(SysConstant.Activity_Close);
//            return;
//        }
//        LuckyDrawValue value = (LuckyDrawValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.luckyDraw);
//        if(value.isParticipation()){
//            player.sendErrorMsg(SysConstant.Activity_Repeat);
//            return;
//        }
//        if(0 != type){
//            int commValue = commValueConfig.getCommValue(CommonValueType.LUCK_DRAW_USED_GOLD);
//            Items item = new Items(PlayerAssetEnum.SysGold.getTypeId(), commValue, ItemType.CURRENCY.getType());
//            if(!itemBiz.checkItemEnoughAndSpend(player, item, LogType.SHARE_REWARD.value(activity.getType()))){
//                player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
//                return;
//            }
//        }
//        value.setParticipation(player,true);
//        activity.addPlayerId(player.getId());
//
//        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_LUCKY_DRAW);
//        serverMsg.addProperty("isParticipation", value.isParticipation());
//        player.sendMsg(serverMsg);
//        player.sendUserUpdateMsg();
//    }

    private void operatorRedisShare(long playerId, int num){
        Jedis jedis = null;
        try {
            jedis = RedisDbUtil.getJedis(RedisConstants.Player);
            String format = StrUtil.format(prefix, playerId);
            jedis.hincrBy(RedisKeyUtils.buildKeyName(format),String.valueOf(playerId),num);
        }finally {
            RedisDbUtil.returnResource(jedis);
        }
    }

    private int getRedisShare(Player player){
        Jedis jedis = null;
        try {
            jedis = RedisDbUtil.getJedis(RedisConstants.Player);
            String format = StrUtil.format(prefix, player.getId());
            String hget = jedis.hget(RedisKeyUtils.buildKeyName(format), String.valueOf(player.getId()));
            return StringUtils.isBlank(hget)? 0 : PubFunc.parseInt(hget);
        }finally {
            RedisDbUtil.returnResource(jedis);
        }
    }
}
