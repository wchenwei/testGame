package com.hm.server;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.enums.KfType;
import com.hm.model.activity.kfactivity.KfServerInfo;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.util.RandomUtils;
import com.hm.util.ServerUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class GameServerType {
	private int id;
	private String mineUrl;//跨服资源战url
	private String expeditionUrls;//跨服远征url
	private String integralUrl;//跨服积分战url
	private String stagematchUrl;//跨服段位赛
	private String solicitUrl;//跨服征讨

	public static GameServerType getGameServerType(int typeId) {
		return MongoUtils.getLoginMongodDB().get(typeId, GameServerType.class, "servertype");
	}
	
	public static List<GameServerType> getAllGameServerType() {
		return MongoUtils.getLoginMongodDB().queryAll(GameServerType.class, "servertype");
	}
	
	public static GameServerType getGameServerTypeByServerId(int serverId) {
		ServerInfo serverInfo = ServerUtils.getServerInfo(serverId);
		if(serverInfo != null) {
			return getGameServerType(serverInfo.getType());
		}
		return null;
	}
	
	public static String getKfMineUrl(int serverId) {
		GameServerType gameServerType = getGameServerTypeByServerId(serverId);
		String mineUrl = gameServerType.getMineUrl();
		if(StrUtil.isNotEmpty(mineUrl)) {
			return mineUrl;
		}
		return null;
	}
	
	public static String getKfExpeditionUrls(int serverId) {
		GameServerType gameServerType = getGameServerTypeByServerId(serverId);
		String kfExpeditionUrls = gameServerType.getExpeditionUrls();
		if(StrUtil.isNotEmpty(kfExpeditionUrls)) {
			return kfExpeditionUrls;
		}
		return null;
	}
	public static String getKfExpeditionUrlsForWeight(int serverId) {
		String url = KfServerInfo.randomKfUrlByWeight(KfType.KfExpedetion.getType());
		if(StrUtil.isNotEmpty(url)) {
			return url;
		}
		return RandomUtils.randomEle(Arrays.asList(getKfExpeditionUrls(serverId).split(",")));
	}
	
	public static String loadKfPkUrl(GameServerType gameServerType) {
		if(gameServerType != null) {
			try {
				String[] pkUrls = gameServerType.getStagematchUrl().split("#");
				return pkUrls[0]+":"+pkUrls[1];
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public static String loadExtortUrl(GameServerType gameServerType) {
		if(gameServerType != null) {
			try {
				String[] pkUrls = gameServerType.getSolicitUrl().split("#");
				return pkUrls[0]+":"+pkUrls[1];
			} catch (Exception e) {
			}
		}
		return null;
	}
}
