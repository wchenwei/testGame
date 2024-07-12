package com.hm.action.trade.biz;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.action.http.biz.BroadServerBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.trade.vo.TradePlayerRankVo;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.fight.FightProxy;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.tank.Tank;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.trade.PlayerStock;
import com.hm.redis.trade.PlayerStockRes;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.util.ServerUtils;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.KFPlayerTroop;
import com.hm.war.sg.troop.PlayerTroop;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Biz
public class TradeStockBiz implements IObserver {
    @Resource
    private MailBiz mailBiz;
    @Resource
    private MailConfig mailConfig;
    @Resource
    private CommValueConfig commValueConfig;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.LOGIN, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.TradeCompanyLvUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.AllServerBroad, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.DayFirstLogin, this);

    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum){
            case LOGIN:
            case TradeCompanyLvUp:
                doTradeCompanyLvUp(player);
                return;
            case AllServerBroad:
                doAllServerBroad(argv);
                return;
            case DayFirstLogin:
                doPlayerDayReset(player);
                return;
        }
    }

    /**
     * 判断是否有大股东
     * @param player
     * @return
     */
    public long calPlayerStock(Player player,long num) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TradeStock)) {
            return num;//没有解锁
        }
        PlayerStock playerStock = PlayerStock.getPlayerStock(player.getId());
        if(playerStock.getOwnerId() <= 0) {
            return num;//没有股东
        }
        double rate = commValueConfig.getDoubleCommValue(CommonValueType.TradeGDShareRate);
        //上缴数量
        long pushNum = Math.min((long)(num * rate),num);
        if(pushNum <= 0) {
            return num;//没有股东
        }
        PlayerStockRes.getPlayerStockRes(playerStock.getOwnerId()).addRes(player.getId(),pushNum);
        playerStock.addVal(pushNum);
        return num - pushNum;
    }


    public void doTradeCompanyLvUp(Player player) {
        if(player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TradeStock)) {
            return;//已经解锁了
        }
        int lv = player.playerShipTrade().getCompany().getLv();
        if(lv < commValueConfig.getCommValue(CommonValueType.TradeGDUnLock)) {
            return;
        }
        player.getPlayerFunction().addOpenFunction(PlayerFunctionType.TradeStock.getType());
        //加入排行
        long combat = player.getPlayerDynamicData().getCombat();
//        HdLeaderboardsService.getInstance().updatePlayerRankOverride(player,RankType.TradeCombatServer,combat);
        HdLeaderboardsService.getInstance().updatePlayerKFRank(player);
        //设置部队
        String topTank = buildTopTank(player);
        PlayerStock.getPlayerStock(player.getId()).setDefTroops(topTank,combat);
    }

    public static String buildTopTank(Player player) {
        List<Tank> topList = player.playerTank().getTankList().stream()
                .sorted(Comparator.comparing(Tank::getCombat).reversed())
                .limit(5).collect(Collectors.toList());
        String tankInfo = "";
        for (int i = 0; i < topList.size(); i++) {
            tankInfo += i +":"+topList.get(i).getId()+",";
        }
        if(tankInfo.length() > 0) {
            tankInfo = tankInfo.substring(0,tankInfo.length()-1);
        }
        return tankInfo;
    }


    /**
     * @author siyunlong
     * @version V1.0
     * @Description: 和玩家战斗
     * @date 2023/8/28 14:27
     */
    public WarResult warTradeStock(Player player,ClientTroop clientTroop,long defPlayerId) {
        PlayerStock defStock = PlayerStock.getPlayerStock(defPlayerId);
        Player defPlayer = PlayerUtils.getPlayerFromKF(defPlayerId);

        PlayerTroop atkTroop = new PlayerTroop(player.getId(), "TradeStock_Atk");
        atkTroop.loadClientTroop(clientTroop);

        KFPlayerTroop defTroop = new KFPlayerTroop(defPlayer.getId(), "TradeStock_Def");
        defTroop.loadClientTroop(defPlayer,ClientTroop.build(defStock.getDefTroops()));

        return new FightProxy().doFight(atkTroop, defTroop);
    }

    /**
     * 发邮件给老的大股东
     * @param ownerId
     * @param atkId
     * @param stockId
     */
    public void sendOwnerChangeMail(long ownerId,long atkId,long stockId) {
        if(GameServerManager.getInstance().isServerMachinePlayer(ownerId)) {
            sendOwnerChangeForCurServer(ownerId,atkId,stockId);
            return;
        }

        Map<String,String> paramMap = Maps.newConcurrentMap();
        paramMap.put("ownerId",ownerId+"");
        paramMap.put("atkId",atkId+"");
        paramMap.put("stockId",stockId+"");
        BroadServerBiz.sendServerMsg(ServerUtils.getCreateServerId(ownerId), AllServerBroadType.TradeStockOwnerChange,paramMap);
    }

    public void doAllServerBroad(Object... argv) {
        int type = (int)argv[0];
        if(AllServerBroadType.TradeStockOwnerChange.getType() != type) {
            return;
        }
        Map<String, String> params = (Map<String, String>)argv[1];
        int ownerId = Convert.toInt(params.get("ownerId"),0);
        if(!GameServerManager.getInstance().isServerMachinePlayer(ownerId)) {
            System.out.println(GSONUtils.ToJSONString(params)+" no player ");
            return;
        }
        int changeType = Convert.toInt(params.get("changeType"),0);
        if(changeType == 0) {
            int atkId = Convert.toInt(params.get("atkId"),0);
            int stockId = Convert.toInt(params.get("stockId"),0);
            sendOwnerChangeForCurServer(ownerId,atkId,stockId);
        }else if(changeType == 1) {
            sendMyTradeStockChangeForCurServer(ownerId);
        }
    }

    public void sendOwnerChangeForCurServer(long ownerId, long atkId, long stockId) {
        String defName = RedisUtil.getPlayerRedisData(stockId).getName();
        String atkName = atkId == stockId?defName:RedisUtil.getPlayerRedisData(atkId).getName();

        //发送邮件
        Player player = PlayerUtils.getPlayer(ownerId);
        MailConfigEnum mailConfigEnum = atkId == stockId ?
                MailConfigEnum.TradeSelfOwnerChange:MailConfigEnum.TradeOwnerChange;


        mailBiz.sendSysMail(player, mailConfigEnum, null, LanguageVo.createStr(defName,atkName));

        player.notifyObservers(ObservableEnum.TradeStockChange,1);
    }


    public List<TradePlayerRankVo> getRankPlayerVoList(Player player,int type) {
        List<Long> rankList = player.playerTradeStock().getRankList(type);
        if(CollUtil.isEmpty(rankList)) {
            RankType rankType = type == 0?RankType.TradeCombatServer:RankType.TradeCombatKF;
            rankList = getKFRankList(player,rankType);
            if(rankList.size() > 10) {
                player.playerTradeStock().setRankList(type,rankList);
            }
        }
//        PlayerStock playerStock = PlayerStock.getPlayerStock(player.getId());
        Map<Long,PlayerRedisData> playerDataMap = RedisUtil.getListPlayer(rankList).stream().collect(Collectors.toMap(PlayerRedisData::getId,e ->e));
        Map<Long,PlayerStock> stockMap = PlayerStock.getPlayerStockForMap(rankList);

        List<TradePlayerRankVo> resultList = Lists.newArrayList();
        for (long pid : rankList) {
            PlayerRedisData redisData = playerDataMap.get(pid);
            PlayerStock playerStock = stockMap.get(pid);
            if(redisData != null && playerStock!= null) {
                TradePlayerRankVo rankVo = new TradePlayerRankVo(pid);
                rankVo.load(redisData);
                rankVo.setSt(playerStock.getOwnerId());
                rankVo.setCombat(playerStock.getCombat());
                resultList.add(rankVo);
            }
        }
        Collections.sort(resultList);
        return resultList;
    }


    /**
     * 获取跨服
     * @param player
     * @return
     */
    public List<Long> getKFRankList(Player player,RankType rankType) {
        int serverId = player.getServerId();
        if(rankType == RankType.TradeCombatKF) {
            ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
            serverId = serverData.getGameServerType().getId();
        }
        return getKFRankList2(player,serverId,rankType);
    }
    public List<Long> getKFRankList2(Player player,int serverId,RankType rankType) {
        int total = 50;
        String rankName = RankRedisUtils.createRankName(serverId, rankType.getRankName(player));
        int rank = (int)RankRedisUtils.getPlayerRank(rankName, player.getId()+"");
        int start = Math.max(rank-total,1);
        int end = rank+total;
        List<LeaderboardInfo> topRanks = HdLeaderboardsService.getInstance().getGameRank(serverId, rankType.getRankName(player), start, end);
        int index = 0;
        for (int i = 0; i < topRanks.size(); i++) {
            if(topRanks.get(i).getIntId() == player.getId()) {
                index = i;break;
            }
        }
        //找出来离我最近的50名
        List<Long> playerList = Lists.newArrayList();
        for (int i = 1; i < total; i++) {
            LeaderboardInfo l1 = getLeaderboardInfo(topRanks,index-i);
            LeaderboardInfo l2 = getLeaderboardInfo(topRanks,index+i);
            if(l1 == null && l2 == null) {
                return playerList;
            }
            if(l1 != null && l1.getIntId() != player.getId()) {
                playerList.add(l1.getIntId());
            }
            if(l2 != null && l2.getIntId() != player.getId()) {
                playerList.add(l2.getIntId());
            }
            if(playerList.size() >= total) {
                return playerList;
            }
        }
        return playerList;
    }

    private LeaderboardInfo getLeaderboardInfo(List<LeaderboardInfo> topRanks,int index) {
        if(index < 0 || index >= topRanks.size()) {
            return null;
        }
        return topRanks.get(index);
    }


    public void doPlayerDayReset(Player player) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TradeStock)) {
            return;//没有解锁
        }
        player.playerTradeStock().doDayReset();//清空昨日对手
        PlayerStockRes playerStockRes = PlayerStockRes.getPlayerStockRes(player.getId());
        System.out.println(player.getId()+"dayresetres:"+GSONUtils.ToJSONString(playerStockRes));
        if(playerStockRes.isEmpty()) {
            return;
        }
        String mark = TimeUtils.formatSimpeTime2(new Date());
        long res = playerStockRes.getTotalRes(mark);
        //计算今日子公司离线收益
        playerStockRes.doOfflinePlayer(commValueConfig);
        playerStockRes.delAndSave();
        //发邮件
        if(res > 0) {
            List<Items> itemsList = Lists.newArrayList();
            itemsList.add(new Items(PlayerAssetEnum.Shipping.getTypeId(), res, ItemType.CURRENCY));

            MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.TradeRes);
            mailBiz.sendSysMail(player,mailTemplate,itemsList);
        }
    }


    /**
     * 我的公司被别人占领了
     * @param ownerId
     * @param atkId
     */
    public void sendMyTradeStockChange(long ownerId,long atkId) {
        if(GameServerManager.getInstance().isServerMachinePlayer(ownerId)) {
            sendMyTradeStockChangeForCurServer(ownerId);
            return;
        }
        Map<String,String> paramMap = Maps.newConcurrentMap();
        paramMap.put("ownerId",ownerId+"");
        paramMap.put("atkId",atkId+"");
        paramMap.put("changeType","1");
        BroadServerBiz.sendServerMsg(ServerUtils.getCreateServerId(ownerId), AllServerBroadType.TradeStockOwnerChange,paramMap);
    }
    public void sendMyTradeStockChangeForCurServer(long ownerId) {
        //发送邮件
        Player player = PlayerUtils.getPlayer(ownerId);
        player.notifyObservers(ObservableEnum.TradeStockChange,0);
    }
}
