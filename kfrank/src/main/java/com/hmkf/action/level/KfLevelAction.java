package com.hmkf.action.level;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.leaderboards.PlayerRankData;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.KFRankMessageComm;
import com.hm.model.item.Items;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hmkf.action.KFAbstractPlayerAction;
import com.hmkf.action.KfClientTroopUtils;
import com.hmkf.action.level.vo.KfPlayerDetailVo;
import com.hmkf.action.npc.NpcPlayer;
import com.hmkf.config.KfRankConfig;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.db.RecordDBUtils;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.model.KFPlayer;
import com.hmkf.model.KfCmType;
import com.hmkf.model.kfwarrecord.KfWarRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class KfLevelAction extends KFAbstractPlayerAction {
    @Resource
    private LevelGroupContainer levelGroupContainer;
    @Resource
    private KfLevelBiz kfLevelBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private KfTopBiz kfTopBiz;
    @Resource
    private KfLevelFightBiz kfLevelFightBiz;
    @Resource
    private KfRankConfig kfRankConfig;
    @Resource
    private KFPlayerContainer kFPlayerContainer;

    @MsgMethod(KFRankMessageComm.C2S_LevelMatch)
    public void levelMatch(KFPlayer player, JsonMsg msg) {
        if (!kfLevelBiz.isCanFightTime()) {
            log.error("levelMatch 不在战斗时间");
            return;
        }
        LevelWorldGroup levelWorldGroup = levelGroupContainer.getLevelWorldGroup(player);
        if (levelWorldGroup == null) {
            log.error("匹配出错:找不到世界组=" + player.getGameTypeId());
            return;
        }
        KfPlayerDetailVo[] matchInfos = player.getLevelPlayerInfo().getMatchInfos();
        if (matchInfos != null) {
            String item = commValueConfig.getStrValue(KfCmType.KfRankRefreshSpend.getType());
            if (!player.addOrSpendPlayerItem(item, false,"pk_match")) {
                return;
            }
        }
        //构建玩家
        KfPlayerDetailVo[] playerVos = kfLevelBiz.buildMatchUser(player);
        player.getLevelPlayerInfo().setMatchInfos(playerVos);
        player.sendPlayerUpdate();

        JsonMsg serverMsg = new JsonMsg(KFRankMessageComm.S2C_LevelMatch);
        kfTopBiz.fillPlayerOppoInfo(player,serverMsg);
        player.sendMsg(serverMsg);
    }

    @MsgMethod(KFRankMessageComm.C2S_getOppInfo)
    public void getOppInfo(KFPlayer player, JsonMsg msg) {
        KfPlayerDetailVo[] matchInfos = player.getLevelPlayerInfo().getMatchInfos();
        if (matchInfos == null) {
            return;
        }
        JsonMsg serverMsg = new JsonMsg(KFRankMessageComm.S2C_getOppInfo);
        kfTopBiz.fillPlayerOppoInfo(player,serverMsg);
        player.sendMsg(serverMsg);
    }

    @MsgMethod(KFRankMessageComm.C2S_LevelFight)
    public void levelFight(KFPlayer player, JsonMsg msg) {
        if (!kfLevelBiz.isCanFightTime()) {
            System.out.println(player.getId()+"不在可战斗时间");
            return;
        }
        int index = msg.getInt("index");

        List<ClientTroop> clientTroopList = KfClientTroopUtils.buildClientTroop(player);
        if (clientTroopList.isEmpty()) {
            System.out.println(player.getId()+"没有玩家部队");
            return;
        }
        KfPlayerDetailVo[] matchInfos = player.getLevelPlayerInfo().getMatchInfos();
        if (matchInfos == null) {
            System.out.println(player.getId()+"没有匹配玩家");
            return;
        }
        KfPlayerDetailVo oppoInfo = matchInfos[index];
        if (oppoInfo == null) {
            System.out.println(player.getId()+"没有匹配玩家"+index);
            return;
        }
        boolean isSuc = kfLevelFightBiz.doFight(player,clientTroopList,oppoInfo);
        if(!isSuc) {
            return;
        }
        player.notifyObservers(ObservableEnum.KFRankFight);
    }


    @MsgMethod(KFRankMessageComm.C2S_LevelFightRevenge)
    public void C2S_LevelFightRevenge(KFPlayer player, JsonMsg msg) {
        if (!kfLevelBiz.isCanFightTime()) {
            System.out.println(player.getId()+"不在可战斗时间");
            return;
        }
        List<ClientTroop> clientTroopList = KfClientTroopUtils.buildClientTroop(player);
        if (clientTroopList.isEmpty()) {
            System.out.println(player.getId()+"没有玩家部队");
            return;
        }
        String id = msg.getString("id");
        KfWarRecord kfWarRecord = RecordDBUtils.getKfWarRecord(id);
        if(kfWarRecord == null) {
            System.out.println(player.getId()+"kfWarRecord is null:"+id);
            return;
        }
        if(!kfWarRecord.isCanRevenge(player.getId())) {
            System.out.println(player.getId()+"不能复仇:"+id);
            return;
        }
        boolean isSuc = kfLevelFightBiz.doFightRevenge(player,clientTroopList,kfWarRecord.getAtkRecord());
        if(!isSuc) {
            return;
        }

        kfWarRecord.setType(1);
        kfWarRecord.save();

        player.notifyObservers(ObservableEnum.KFRankFight);
    }

    @MsgMethod(KFRankMessageComm.C2S_BuyCount)
    public void buyCount(KFPlayer player, JsonMsg msg) {
        if (!kfLevelBiz.isCanFightTime()) {
            return;
        }
        int count = msg.getInt("count");
        List<Items> itemList = ItemUtils.str2DefaultItemList(commValueConfig.getStrValue(KfCmType.KfRankBuySpend.getType()));
        itemList = ItemUtils.calItemRateReward(itemList,count);

        if (!player.addOrSpendPlayerItem(ItemUtils.itemListToString(itemList), false,"pk_buy")) {
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        player.getLevelPlayerInfo().buyCount(count);
        player.sendPlayerUpdate();

        player.sendMsg(KFRankMessageComm.S2C_BuyCount);
    }

    @MsgMethod(KFRankMessageComm.C2S_KfRecord)
    public void sportsRecord(KFPlayer player, JsonMsg msg) {
        List<KfWarRecord> recordList = RecordDBUtils.getKfWarRecord(player.getId());
        JsonMsg serverMsg = JsonMsg.create(KFRankMessageComm.S2C_KfRecord);
        serverMsg.addProperty("recordList", recordList);
        player.sendMsg(serverMsg);
    }


    @MsgMethod(KFRankMessageComm.C2S_GameRank)
    public void getGameRank(KFPlayer player, JsonMsg msg) {
        int pageNo = msg.getInt("pageNo");
        pageNo = Math.max(1, pageNo);
        String rankName = player.getRankId();
        int leaderIndex = 0;

        List<LeaderboardInfo> rankList = HdLeaderboardsService.getInstance().getGameRank(leaderIndex, rankName, pageNo);
        for (LeaderboardInfo leaderboardInfo : rankList) {
            rebuildLeaderboardInfo(leaderboardInfo);
        }

        JsonMsg serverMsg = JsonMsg.create(KFRankMessageComm.S2C_GameRank);
        serverMsg.addProperty("pageNo", pageNo);
        serverMsg.addProperty("ranks", rankList);
        if (pageNo == 1) {
            List<LeaderboardInfo> tempList = HdLeaderboardsService.getInstance().getGameRankByPlayerIds(leaderIndex, rankName, Lists.newArrayList(player.getId()));
            if (CollUtil.isNotEmpty(tempList)) {
                LeaderboardInfo temp = tempList.get(0);
                rebuildLeaderboardInfo(temp);
                serverMsg.addProperty("myRankData", temp);
            }
        }
        player.sendMsg(serverMsg);
    }

    public void rebuildLeaderboardInfo(LeaderboardInfo leaderboardInfo) {
        leaderboardInfo.scoreToLong();
        if(leaderboardInfo.isNpc()) {
            NpcPlayer npcPlayer = KFNpcContainer.getNpcPlayer(leaderboardInfo.getId());
            PlayerRankData playerRankData = new PlayerRankData();
            NpcTroopTemplate template = kfRankConfig.getArenaNpc(npcPlayer.getNpcId()).getFirstNpcTemplate();

            playerRankData.loadNpc(template);
            playerRankData.setCombat(npcPlayer.getCombat());
            playerRankData.setName(npcPlayer.getName());
            leaderboardInfo.setPlayerRankData(playerRankData);
        }else{
            PlayerRankData playerRankData = leaderboardInfo.getPlayerRankData();
            playerRankData.setCombat(leaderboardInfo.getPlayerData().getTroopCombat());
        }

    }

    @MsgMethod(KFRankMessageComm.C2S_LeaveKf)
    public void C2S_LeaveKf(KFPlayer player, JsonMsg msg) {
        kFPlayerContainer.removePlayer(player.getId());
    }
}
