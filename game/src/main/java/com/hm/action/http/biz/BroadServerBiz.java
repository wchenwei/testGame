package com.hm.action.http.biz;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.hm.actor.ActorDispatcherType;
import com.hm.enums.AllServerBroadType;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.ServerInfoCache;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.GameIpUtils;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class BroadServerBiz {

	

	/**
	 * 全服广播
	 * @param type
	 * @param params
	 */
	public void broadAllServerMsg(AllServerBroadType type,Map<String,String> params) {
		ThreadUtil.execute(new Runnable() {
			@Override
			public void run() {
				for (String url: createAllMachineUrl()) {
					try {
						log.error("全服消息:"+url);
						Map<String , Object> paramMap = Maps.newConcurrentMap();
						paramMap.put("action","kf.do");
						paramMap.put("m","doKfEventMsg");
						paramMap.put("msgId","broadAllServerMsg");
						paramMap.put("type",type.getType());
						paramMap.put("params",Base64.encode(GSONUtils.ToJSONString(params)));
						HttpUtil.post(url, paramMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * 给某个服发送指定消息
	 * @param serverId
	 * @param type
	 * @param params
	 */
	public static void sendServerMsg(int serverId,AllServerBroadType type,Map<String,String> params) {
		GameServerMachine gameServerMachine = ServerInfoCache.getInstance().getServerInfo(serverId).getServerMachine();
		if(gameServerMachine == null) {
			return;
		}
		ActorDispatcherType.Http.putTask(new IRunner() {
			@Override
			public Object runActor() {
				Map<String , Object> paramMap = Maps.newConcurrentMap();
				paramMap.put("action","kf.do");
				paramMap.put("m","doKfEventMsg");
				paramMap.put("msgId","broadAllServerMsg");
				paramMap.put("type",type.getType());
				paramMap.put("params",Base64.encode(GSONUtils.ToJSONString(params)));
				HttpUtil.post(GameIpUtils.getGameRpcUrl(gameServerMachine), paramMap);
				return null;
			}
		});
	}
	
	public void doBroadAllServerMsg(HttpSession session) {
		try {
			String params = Base64.decodeStr(session.getParams("params"));
			int type = Integer.parseInt(session.getParams("type"));
			
			log.error("收到跨服消息:"+type+"="+params);
			Map<String,String> parmMap = GSONUtils.FromJSONString(params, new TypeToken<Map<String,String>>(){}.getType());
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.AllServerBroad, null, type,parmMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 全服广播中奖记录
	 * @param playerName
	 * @param
	 */
	public void broadEggTrueItem(String playerName,String itemName) {
		Map<Integer,String> serverMap = createAllServerUrl();
		for (Map.Entry<Integer,String> entry: serverMap.entrySet()) {
			try {
				Map<String , Object> paramMap = Maps.newConcurrentMap();
				paramMap.put("action","kf.do");
				paramMap.put("m","doKfEventMsg");
				paramMap.put("msgId","sendEggTrueItem");
				paramMap.put("playerName",playerName);
				paramMap.put("itemName",itemName);
				paramMap.put("serverId",entry.getKey());
				
				HttpUtil.post(entry.getValue(), paramMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static Map<Integer,String> createAllServerUrl() {
		Map<Integer,String> urlMap = Maps.newHashMap();
		Map<Integer, GameServerMachine> machineMap = MongoUtils.getLoginMongodDB()
				.queryAll(GameServerMachine.class,"serverMachine")
				.stream().collect(Collectors.toMap(GameServerMachine::getId, e -> e));
		List<ServerInfo> allServer = ServerUtils.getAllServerInfoFromDB()
				.stream().filter(e -> e.getDb_id() == 0 || e.getDb_id() == e.getServer_id())
				.collect(Collectors.toList());
		for (ServerInfo serverInfo : allServer) {
			GameServerMachine gameServerMachine = machineMap.get(serverInfo.getServermachineId());
			if(gameServerMachine != null) {
				urlMap.put(serverInfo.getServer_id(), "http://"+ gameServerMachine.getHost()+":"+ gameServerMachine.getHttp_port());
			}
		}
		return urlMap;
	}
	
	public static List<String> createAllMachineUrl() {
		return MongoUtils.getLoginMongodDB()
				.queryAll(GameServerMachine.class,"serverMachine")
				.stream().filter(e -> !e.isTestServer())
				.map(e -> GameIpUtils.getGameRpcUrl(e)).collect(Collectors.toList());
	}
	
}
