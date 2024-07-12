/**
 * Project Name:SLG_GameFuture.
 * File Name:PlayerAction.java
 * Package Name:com.hm.action.player
 * Date:2017年12月27日下午3:04:46
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.
 *
 */
package com.hm.action.player;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.reflect.TypeToken;
import com.hm.action.guild.vo.GuildDetailVo;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.bag.BagBiz;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.http.biz.HttpBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.player.biz.CombatBiz;
import com.hm.action.player.biz.PlayerNameBiz;
import com.hm.action.sys.SysFacade;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.GameConstants;
import com.hm.config.PlayerHeadConfig;
import com.hm.config.excel.*;
import com.hm.config.excel.templaextra.AdTemplate;
import com.hm.config.excel.templaextra.PlayerHeadExtraTemplate;
import com.hm.config.excel.templaextra.ShareTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.action.player.vo.PlayerDetailVo;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.player.PlayerDataServerContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.*;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * ClassName:PlayerAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年12月27日 下午3:04:46 <br/>
 * @author zqh
 * @version 1.1
 * @since
 */
@Slf4j
@Action
public class PlayerAction extends AbstractPlayerAction {
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private LogBiz logBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private PlayerHeadConfig playerHeadConfig;
    @Resource
    private TankBiz tankBiz;
    @Resource
    private CmqBiz cmpBiz;
    @Resource
    private DropConfig dropConfig;
    @Resource
    private GiftPackageConfig giftPackageConfig;
    @Resource
    private HttpBiz httpBiz;
    @Resource
    private ActivityBiz activityBiz;
    @Resource
    private LoginBiz loginBiz;
    @Resource
    private SysFacade sysFacade;
    @Resource
    private RemovePlayerBiz removePlayerBiz;
    @Resource
    private PlayerNameBiz playerNameBiz;
    @Resource
    private AdConfig adConfig;
    @Resource
    private ShareConfig shareConfig;


    private static int topTankSize = 10;
    /**
     * 第一次创建名字（新手引导）
     */
    @MsgMethod(MessageComm.C2S_Player_Create_Name)
    public void createPlayerName(Player player, JsonMsg msg) {
        String name = NameUtils.nameClearBlank(msg.getString("name"));

        boolean isUtf8 = Utf8CheckUtil.isUTF8(name.getBytes());
        if (!isUtf8) {
            player.sendErrorMsg(SysConstant.FORBIDDEN_CHARACTER);
            return;
        }
        if (!PlayerDataServerContainer.of(player).isContainsRandom(name)&&!NameUtils.isFitName(name)) {
            player.sendErrorMsg(SysConstant.NAME_ILLEGAL);
            return;
        }
        // 合服服务器用户名字自动加上服务器前缀
        name = HFUtils.checkName(player, name);

        if (!PlayerUtils.checkName(name, player.getServerId())) {
            player.sendErrorMsg(SysConstant.PLAYER_NAME_RE);
            return;
        }
        String oldName = player.getName();
        playerBiz.changeName(player, name);
        //改变
        player.notifyObservers(ObservableEnum.ChangeName, oldName);
        //聊天改名字
        player.sendMsg(MessageComm.S2C_Player_Rename, SysConstant.YES);
        player.sendUserUpdateMsg();
    }
    /**
     * 修改名称
     *
     */
    @MsgMethod(MessageComm.C2S_Player_Rename)
    public void modifyPlayerName(Player player, JsonMsg msg) {
        String name = NameUtils.nameClearBlank(msg.getString("name"));
        boolean isSpend = player.getPlayerStatistics().getLifeStatistics(StatisticsType.ReNameCount) > 0;

        //是否禁止修改昵称
        if (isSpend && UpdateLockUtil.isNoChange()) {
            return;
        }
        if (!PlayerDataServerContainer.of(player).isContainsRandom(name)&&!NameUtils.isFitName(name)) {
            player.sendErrorMsg(SysConstant.NAME_ILLEGAL);
            return;
        }
        // 合服服务器用户名字自动加上服务器前缀
        name = HFUtils.checkName(player, name);
        if (!PlayerUtils.checkName(name, player.getServerId())) {
            player.sendErrorMsg(SysConstant.PLAYER_NAME_RE);
            return;
        }
        if(isSpend){
            int cost = commValueConfig.getCommValue(CommonValueType.ChangeNamePrice);
            if(cost>0){//扣钱
                if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold,cost, LogType.RENAME)){
                    player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
                    return;
                }
            }
        }
        String oldName = player.getName();
        playerBiz.changeName(player, name);
        //改变
        player.notifyObservers(ObservableEnum.ChangeName, oldName);
        player.getPlayerStatistics().addLifeStatistics(StatisticsType.ReNameCount);
        //聊天改名字
        player.sendMsg(MessageComm.S2C_Player_Rename, SysConstant.YES);
        player.sendUserUpdateMsg();
    }

    @MsgMethod(MessageComm.C2S_Player_UnlockIcon)
    public void unlockPlayerIcon(Player player, JsonMsg msg) {
        int icon = msg.getInt("icon");
        if(player.playerHead().isUnlockIcon(icon)){
            //已经解锁
            return;
        }
        PlayerHeadExtraTemplate headExtraTemplate = playerHeadConfig.getHead(icon);
        if (headExtraTemplate == null) {
            return;
        }
        if (headExtraTemplate.getCost_general() > 0) {
            if (tankBiz.getDriverLv(player, headExtraTemplate.getCost_general()) < GameConstants.Drive_Head_Lv) {
                player.sendErrorMsg(SysConstant.Player_Level_Not_Enough);
                return;
            }
        }

        Items cost = playerHeadConfig.getUnlockHeadCost(icon);
        if(cost!=null&&cost.getCount()>0){//扣钱
            if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.UnlockIcon.value(icon))){
                player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
                return;
            }
        }
        //解锁头像
        player.playerHead().unlockIcon(icon);
        player.notifyObservers(ObservableEnum.UnlockIcon,icon);
        player.playerHead().changeIcon(icon);
        player.notifyObservers(ObservableEnum.ChangeIcon);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_UnlockIcon, icon);
    }
    @MsgMethod(MessageComm.C2S_Player_ChangeIcon)
    public void changePlayerIcon(Player player, JsonMsg msg) {
        int icon = msg.getInt("icon");
        if(!player.playerHead().isUnlockIcon(icon)){
            //头像还没有解锁
            return;
        }
        player.playerHead().changeIcon(icon);
        player.notifyObservers(ObservableEnum.ChangeIcon);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_ChangeIcon, icon);
    }
    @MsgMethod(MessageComm.C2S_Player_ChangeFrame)
    public void changePlayerFrameIcon(Player player, JsonMsg msg) {
        int iconFrame = msg.getInt("iconFrame");
        if(!player.playerHead().isUnlockFrameIcon(iconFrame)){
            //头像框还没有解锁
            return;
        }
        player.playerHead().changeHeadFrameIcon(iconFrame);
        player.notifyObservers(ObservableEnum.ChangeFrameIcon);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_ChangeFrame, iconFrame);
    }


    @MsgMethod(MessageComm.C2S_Player_Vo)
    public void getPlayerVo(Player player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        Player tarPlayer = PlayerUtils.getPlayer(playerId);
        List<WorldTroop> troopList = TroopServerContainer.of(player).getWorldTroopByPlayer(tarPlayer);

        PlayerDetailVo playerVo = new PlayerDetailVo(tarPlayer);
        playerVo.loadTroopList(tarPlayer,troopList);
        
        player.sendMsg(MessageComm.S2C_Player_Vo, playerVo);
    }


    @MsgMethod(MessageComm.C2S_GuildDetailVo)
    public void getGuildVo(Player player,JsonMsg msg) {
        int guildId = msg.getInt("id");
        Guild guild = GuildContainer.of(player).getGuild(guildId);
        if(guild == null) {
            return;
        }
        player.sendMsg(MessageComm.S2C_GuildDetailVo, GuildDetailVo.buildVo(guild));
    }
    
    

    @MsgMethod(MessageComm.C2S_PlayerGuide)
    public void changePlayerGuide(Player player, JsonMsg msg) {
        String mainId = msg.getString("mainId");
        int subId = msg.getInt("subId");
        player.playerGuide().changeGuide(mainId, subId);
        player.sendUserUpdateMsg();

    }

    @MsgMethod(MessageComm.C2S_ChangeSpeGuide)
    public void changeSpeGuide(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        int value = msg.getInt("value");
        player.playerGuide().changeSpeGuide(id, value);
        player.sendUserUpdateMsg();
    }

    @MsgMethod(MessageComm.C2S_GuideStep)
    public void doGuideStep(Player player, JsonMsg msg) {
        int step = msg.getInt("step");
        player.notifyObservers(ObservableEnum.CHPlayerStep, step);
    }

    @MsgMethod(MessageComm.C2S_ClientUpSdk)
    public void doClientUpSdk(Player player, JsonMsg msg) {
        String groupId = msg.getString("groupId");
        String groupName = msg.getString("groupName");
        String eventId = msg.getString("eventId");
        String eventName = msg.getString("eventName");


        Map<String, Object> hash = null;
        if (msg.containsKey("eventParam")) {
            String eventParam = msg.getString("eventParam");
            if (StrUtil.isNotEmpty(eventParam)) {
                hash = GSONUtils.FromJSONString(eventParam, new TypeToken<Map<String, Object>>() {
                }.getType());
            }
        }
        player.notifyObservers(ObservableEnum.CHClientUp, groupId, groupName, eventId, eventName, hash);
    }


    /**
     * reportTarPlayer:(举报其他用户). <br/>
     *
     * @param player
     * @param msg    使用说明
     *               #msg:2508,tarPlayer=544554,content=12121221212121212
     * @author zxj
     */
    @MsgMethod(MessageComm.S2C_ReportTar)
    public void reportTarPlayer(Player player, JsonMsg msg) {
        int tarPlayerId = msg.getInt("tarPlayer");
        String content = msg.getString("content");
        cmpBiz.sendReportPlayer(player, tarPlayerId, content);
        player.playerChat().addBlack(tarPlayerId);
        player.sendUserUpdateMsg();
        //向gm后台发送举报信息，检查被举报人是否发送不当言论
        ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
        int openDay = serverData.getServerStatistics().getOpenDay();
        //如果玩家等级>=30+2*(openDay-1)则不进行过滤字检查，最高检查到50级
        int lvLimit = Math.min(30+2*(openDay-1), GameConstants.WordFilter_LvLimit);
        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(tarPlayerId);
        if(redisData!=null) {
            if(redisData.getLv()<lvLimit) {
                httpBiz.report(tarPlayerId,content);
            }

        }
        player.sendMsg(MessageComm.C2S_ReportTar, true);
    }
    /**
     * 绑定手机号
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Bind_Phone)
    public void bindPhone(Player player, JsonMsg msg) {
        String phone = msg.getString("phone");
        if(!StringUtil.isSimplePhone(phone)){
            player.sendErrorMsg(SysConstant.Phone_Error);
            return;
        }
        //同步到redis
        RedisUtil.updateRedisBindPhone(player, phone);
        player.playerBaseInfo().bindPhone(phone);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Bind_Phone);
        player.notifyObservers(ObservableEnum.BindPhone, phone);
    }
    /**
     * 领取绑定手机号奖励
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_BindPhone_Receive)
    public void bindPhoneReceive(Player player, JsonMsg msg) {
        if(player.playerBaseInfo().isBindRewardFlag()){
            player.sendErrorMsg(SysConstant.PhoneReward_Received);
            return;
        }
        int giftId = commValueConfig.getCommValue(CommonValueType.BindPhoneGiftId);
        List<Items> rewards = giftPackageConfig.rewardGiftList(giftId);
        itemBiz.addItem(player, rewards, LogType.BindPhone);
        player.playerBaseInfo().receiveBindReward();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_BindPhone_Receive,rewards);
        activityBiz.sendActivityList(player);
    }
    /**
     * 请求是否能够跳过新手引导
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_SkipInfo)
    public void getPlayerSkipInfo(Player player, JsonMsg msg){
        int maxLv = httpBiz.getUserMaxLv(player.getUid());
        player.sendMsg(MessageComm.S2C_SkipInfo, maxLv>=60);
    }

    /**
     * 跳过新手引导
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_SkipGuide)
    public void skipGuide(Player player, JsonMsg msg){
        if(player.playerLevel().getLv()>1){
            return;
        }
        if(httpBiz.getUserMaxLv(player.getUid())<60){
            return;
        }
        //先解锁城市发放给客户端
        player.playerMission().setMissionId(GameConstants.Skip_Guide_Mission);
        int openCity = player.playerMission().getRelOpenCity();
        for(int i=1;i<=openCity;i++){
            player.playerMission().checkArmory(i);
        }
        player.playerGuide().skipGuide();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_SkipGuide);
        //再将奖励发放
        List<Items> rewards = commValueConfig.getListItem(CommonValueType.SkipGuideRewards);
        itemBiz.addItem(player, rewards, LogType.SkipGuide);
        player.playerTank().getTankList().forEach(tank ->{
            tankBiz.lvUpTank(player, tank.getId());
            tankBiz.reform(player, tank);
        });
        player.notifyObservers(ObservableEnum.RecaptureCity, player.playerMission().getOpenCity());
        player.sendUserUpdateMsg();
    }

    @MsgMethod(MessageComm.C2S_RemovePlayer)
    public void removePlayer(Player player, JsonMsg msg) {
        long clearPlayerTime = removePlayerBiz.addClearPlayer(player);
        log.error(player.getId() + " 注销角色");
        // 增加行为日志
        logBiz.addPlayerActionLog(player, ActionType.RemovePlayer, DateUtil.toStr(clearPlayerTime));

        sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);

    }

    @MsgMethod(MessageComm.C2S_Player_RandomName)
    public void randomPlayerName(Player player, JsonMsg msg){
        String name = playerNameBiz.randomPlayerName(player.getServerId());
        player.sendMsg(MessageComm.S2C_Player_RandomName,name);
        PlayerDataServerContainer.of(player).addRandomName(name);

    }

    @MsgMethod(MessageComm.C2S_Player_ShowAd)
    public void showAd(Player player, JsonMsg msg){
        int id = msg.getInt("id");//广告id
        if(player.playerAds().haveAds(id)) {
            return;
        }
        AdTemplate adTemplate = adConfig.getTemplate(id);
        if(adTemplate == null) {
            log.error(player.getId()+" 广告不存在:"+id);
        }
        AdsType adsType = adTemplate.getAdsType();
        if(adsType == null) {
            log.error(player.getId()+" 广告类型不存在:"+id);
            return;
        }

        // 该广告配置了cd,记录触发时间
        if (adTemplate.getCd_time() > 0) {
            player.playerAds().updateAdCd(id, System.currentTimeMillis()+adTemplate.getCd_time()*GameConstants.SECOND);
        }

        player.playerAds().addAd(id);
        player.notifyObservers(ObservableEnum.AdShow, id);
        if(adsType.isAfterReward()) {
            adReward(player,msg);
            return;
        }
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_ShowAd);
    }
    @MsgMethod(MessageComm.C2S_Player_AdReward)
    public void adReward(Player player, JsonMsg msg){
        int id = msg.getInt("id");//广告id
        AdTemplate adTemplate = adConfig.getTemplate(id);
        if(adTemplate == null) {
            log.error(player.getId()+" 广告不存在:"+id);
        }
        AdsType adsType = adTemplate.getAdsType();
        if(adsType == null) {
            log.error(player.getId()+" 广告类型不存在:"+id);
            return;
        }
        int maxCount = adTemplate.getMax_count();
        boolean checkCount = maxCount > 1;
        if(checkCount && maxCount <= player.playerAds().getTodayCount(id)) {
            log.error(player.getId()+" 广告类型最大次数:"+id+"->"+maxCount);
            return;//已经最大次数了
        }
        if(!adTemplate.isNowReward() && !player.playerAds().rewardAds(id)) {
            return;
        }
        List<Items> itemsList = adsType.reward(player,adTemplate,msg);
        if(CollUtil.isNotEmpty(itemsList)) {
            itemBiz.addItem(player,itemsList,LogType.AdReward.value(id));
        }
        if(checkCount) {//添加次数
            player.playerAds().addAdCount(id);
        }
        //记录cd
        if (adTemplate.isNowReward() && adTemplate.getCd_time() > 0) {
            player.playerAds().updateAdCd(id, System.currentTimeMillis()+adTemplate.getCd_time()*GameConstants.SECOND);
        }
        player.sendUserUpdateMsg();

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Player_AdReward);
        retMsg.addProperty("id",id);
        retMsg.addProperty("itemsList",itemsList);
        player.sendMsg(retMsg);

    }

    @MsgMethod(MessageComm.C2S_Player_Share)
    public void share(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        ShareTemplate template = shareConfig.getTemplate(id);
        // 有时限的记录时间
        if (null == template || !player.playerShare().checkCd(id, template.getShare_cd())) {
            return;
        }
        // 已成功分享次数
        final int oldShareCnt = player.playerShare().getShareCnt(id);
        if (oldShareCnt >= template.getCount_need() * template.getLimit_num()) {
            // 分享次数超上限
            return;
        }
        if (template.getShare_cd() > 0) {
            player.playerShare().addCd(id);
        }
        // 更新分享次数
        player.playerShare().addRec(id);
        final int shareCnt = player.playerShare().getShareCnt(id);
        if (shareCnt % template.getCount_need() == 0) {
            template.getShareType().onShare(player, template);
        }
        player.playerShare().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_Share);
    }

    @MsgMethod(MessageComm.C2S_Player_ShareReward)
    public void shareReward(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        ShareTemplate template = shareConfig.getTemplate(id);
        if (template == null) {
            return;
        }
        final int shareCnt = player.playerShare().getShareCnt(id);
        if (shareCnt == 0 || shareCnt % template.getCount_need() != 0) {
            return;
        }

        List<Items> itemsList = template.getShareType().onShareReward(player, template);
        if (CollUtil.isNotEmpty(itemsList)) {
            itemBiz.addItem(player, itemsList, LogType.ShareReward.value(id));
            player.sendUserUpdateMsg();
            JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Player_ShareReward);
            retMsg.addProperty("id", id);
            retMsg.addProperty("itemsList", itemsList);
            player.sendMsg(retMsg);
        }
    }

    @MsgMethod(MessageComm.C2S_ClientItemNotEnough)
    public void doClientItemNotEnough(Player player, JsonMsg msg) {
        int type = msg.getInt("type");
        int id = msg.getInt("id");

        player.notifyObservers(ObservableEnum.ItemNotEnough,type,id);
    }
}
