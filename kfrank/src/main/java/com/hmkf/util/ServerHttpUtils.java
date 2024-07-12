package com.hmkf.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.ok.HttpRequestMap;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.kf.pk.ServerLevelReward;
import com.hm.enums.KfType;
import com.hm.util.GameIpUtils;
import com.hm.war.sg.troop.TankArmy;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.leaderboard.KfPlayerRank;
import com.hmkf.server.ServerGroupManager;
import com.hm.libcore.mongodb.ServerInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ServerHttpUtils {
    public static final int HttpTimeOut = 3000;

    public static boolean addOrSpendPlayerGold(long playerId, int gold) {
        try {
            if (gold == 0) {
                return true;
            }
            ServerInfo serverInfo = ServerGroupManager.getIntance().getServerInfo(playerId);
            if (serverInfo == null) {
                return false;
            }
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("action", "kf.do");
            paramMap.put("m", "addPlayerDiamond");
            paramMap.put("playerId", playerId);
            paramMap.put("gold", gold);
            paramMap.put("kfId", KfType.PKLevel.getType());
//			String result = HttpUtil.get(createUrl(serverInfo), paramMap,HttpTimeOut);
            String result = sendPost(serverInfo, paramMap);
            return StrUtil.equals(result, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean sendKfLevelPlayerServerReward(ServerLevelReward serverLevelReward) {
        try {
            String json = GSONUtils.ToJSONString(serverLevelReward);
            log.error("赛季结束发送" + json);
            ServerInfo serverInfo = ServerGroupManager.getIntance().getServerInfoByServerId(serverLevelReward.getServerId());
            if (serverInfo == null) {
                return false;
            }
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("action", "kfnew.do");
            paramMap.put("m", "sendKfLevelPlayerServerReward");
            paramMap.put("serverId", serverLevelReward.getServerId());
            paramMap.put("snum", KFLevelConstants.SNum);
            paramMap.put("serverLevelReward", json);
//			String suc = HttpUtil.post(createUrl(serverInfo), paramMap,HttpTimeOut);
            String suc = sendPost(serverInfo, paramMap);
            return StrUtil.equals("1", suc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendKfLevelChangeKing(String url, List<Integer> serverIds, List<Integer> kings) {
        try {
            if(url.contains("127.0.0.1")) {
                return false;
            }
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("action", "kfnew.do");
            paramMap.put("m", "sendKfLevelChangeKing");
            paramMap.put("serverIds", StringUtil.list2Str(serverIds, "_"));
            paramMap.put("kings", StringUtil.list2Str(kings, "_"));
//			String suc = HttpUtil.post("http://"+url, paramMap,HttpTimeOut);
            String suc = HttpRequestMap.create("http://" + url).putAllMap(paramMap).sendPost();
            return StrUtil.equals("1", suc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean addOrSpendPlayerItem(long playerId, String items, boolean isAdd) {
        try {
            if (StrUtil.isEmpty(items)) {
                return false;
            }
            ServerInfo serverInfo = ServerGroupManager.getIntance().getServerInfo(playerId);
            if (serverInfo == null) {
                return false;
            }
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("action", "kf.do");
            paramMap.put("m", "addOrSpendPlayerItem");
            paramMap.put("playerId", playerId);
            paramMap.put("items", items);
            paramMap.put("addType", isAdd ? 0 : 1);
            paramMap.put("kfId", KfType.PKLevel.getType());
//			String result = HttpUtil.get(createUrl(serverInfo), paramMap,HttpTimeOut);
            String result = sendPost(serverInfo, paramMap);
            return StrUtil.equals(result, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendRankReward(int rankType, int serverId, List<KfPlayerRank> valueList) {
        try {
            ServerInfo serverInfo = ServerGroupManager.getIntance().getServerInfoByServerId(serverId);
            if (serverInfo == null) {
                return false;
            }
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("action", "kf.do");
            paramMap.put("m", "doKfEventMsg");
            paramMap.put("msgId", "sendRankReward");
            paramMap.put("rankType", rankType);
            paramMap.put("serverId", serverId);
            paramMap.put("valueList", GSONUtils.ToJSONString(valueList));
//			HttpUtil.post(createUrl(serverInfo), paramMap,HttpTimeOut);
            sendAsynPost(serverInfo, paramMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean reduceTankOil(long playerId, List<TankArmy> tankList) {
        try {
            ServerInfo serverInfo = ServerGroupManager.getIntance().getServerInfo(playerId);
            if (serverInfo == null) {
                return false;
            }
            Map<String, Object> paramMap = Maps.newConcurrentMap();
            paramMap.put("action", "kf.do");
            paramMap.put("m", "reduceTankOil");
            paramMap.put("playerId", playerId);
            paramMap.put("extra", "kfrank");
            paramMap.put("tankIds", StringUtil.list2Str(tankList.stream().map(e -> e.getId()).collect(Collectors.toList()), ","));
            paramMap.put("oilDiscount", 1);//石油打折
            paramMap.put("extraInfo", KfType.PKLevel.getType());
//			String result = HttpUtil.post(createUrl(serverInfo), paramMap,HttpTimeOut);
            String result = sendPost(serverInfo, paramMap);
            return StrUtil.equals(result, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String createUrl(ServerInfo serverInfo) {
        return GameIpUtils.getGameRpcUrl(serverInfo.getServerMachine());
    }

    public static String sendPost(ServerInfo serverInfo, Map<String, Object> paramMap) {
        return HttpRequestMap.create(createUrl(serverInfo)).putAllMap(paramMap).sendPost();
    }

    public static void sendAsynPost(ServerInfo serverInfo, Map<String, Object> paramMap) {
        HttpRequestMap.create(createUrl(serverInfo)).putAllMap(paramMap).sendAsynPost();
    }

    public static void main(String[] args) {
        ServerGroupManager.getIntance().init();
//		List<KfPlayerRank> valueList = Lists.newArrayList();
//		valueList.add(new KfPlayerRank(600009, 1));
//		sendRankReward(RankType.ResScoreRank.getType(), 6, valueList);
//		addOrSpendPlayerGold(600009, -1);
        addOrSpendPlayerItem(600062, "1:1:100", true);
    }
}
