package com.hm.util;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.SrvTMgrUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerDataUtils;
import com.hm.model.serverpublic.ServerStatistics;
import com.hm.libcore.mongodb.MongoUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * User: yang xb
 * Date: 2019-05-25
 */
public class HFUtils {

    private static final Pattern jzRegex = Pattern.compile("^局座\\d+服.*");
    private static final Pattern qlRegex = Pattern.compile("^麒麟\\d+服.*");
    private static final Pattern dfRegex = Pattern.compile("^东风\\d+服.*");
    private static final Pattern xfRegex = Pattern.compile("^先锋\\d+服.*");
    private static final Pattern idRegex = Pattern.compile("\\d+");

    private static Set<Pattern> patternSet = Sets.newHashSet();
    static {
        Set<String> marks = SrvTMgrUtils.getMarks();
        for (String mark : marks) {
            String regex = String.format("^%s\\d+服.*", mark);
            patternSet.add(Pattern.compile(regex));
        }
    }

    public static String checkName(Player player,String name) {
		if (ServerUtils.isMergeServer(player.getServerId())) {
			return checkName(name, player.getCreateServerId(), MongoUtils.serverNameInfo());
		}
		return name;
	}
	
	public static String checkName(Guild guild,String name) {
		if (ServerUtils.isMergeServer(guild.getServerId())) {
			int createServerId = ServerUtils.getCreateServerId(guild.getGuildInfo().getCreateUserId());
			return checkName(name, createServerId, MongoUtils.serverNameInfo());
		}
		return name;
	}
	
    /**
     * 校验用户名字 名字规则 `[创建createServerId 对应的serverName] + 用户昵称`
     *
     * @param name
     * @param createServerId
     * @param serverNameInfo
     * @return
     */
    public static String checkName(String name, Integer createServerId, Map<Integer, String> serverNameInfo) {
        // serverName = `ab-def`
        // serverName = 局座23服 -->局23
        String serverName = serverNameInfo.getOrDefault(createServerId, "");
        String s;
        // 局座888服
        if (isMatch(serverName)) {
            int i = serverName.indexOf("服");
            // 局座23服
            String substring = serverName.substring(0, i + 1);
            Matcher m = idRegex.matcher(substring);
            if (!m.find()) {
                return name;
            }
            // 局23
            s = String.format("%s%s", serverName.substring(0, 1), m.group(0));
        } else if (serverName.contains("-")) {
            s = serverName.split("-")[0];
        } else {
            s = serverName;
        }

        String prefix = "[" + s + "]";
        if (name.startsWith(prefix)) {
            return name;
        } else {
            return prefix + name;
        }
}

    public static boolean isMatch(String serverName) {
        for (Pattern pattern : patternSet) {
            if (pattern.matcher(serverName).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 名字是否以[xxx] 开头
     *
     * @param name
     * @return
     */
    public static boolean haveMergeServerPrefix(String name) {
        return !StringUtil.isNullOrEmpty(name) && name.matches("^\\[.*].*");
    }

    /**
     * 重新计算服务器等级
     *
     * @param serverId
     */
    public static void calServerLv(int serverId) {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        int endRank = commValueConfig.getCommValue(CommonValueType.ServerLvRate);
        endRank = Math.max(1, endRank);
        ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics();
        List<LeaderboardInfo> tempList = HdLeaderboardsService.getInstance().getGameRank(serverId, RankType.Combat.getRankName(), 1, endRank);
        if(CollUtil.isNotEmpty(tempList)) {
        	try {
        		 int serverLv = (int) tempList.stream().mapToInt(e -> e.getPlayerData().getLv()).average().orElse(0);
                 serverStatistics.setServerLv(serverLv);
                 serverStatistics.setBaseNpcLv(ServerDataUtils.calBaseLv(serverId));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        serverStatistics.save();
    }

    public static void reloadMark() {
        patternSet.clear();
        Set<String> marks = SrvTMgrUtils.getMarks();
        for (String mark : marks) {
            String regex = String.format("^%s\\d+服.*", mark);
            patternSet.add(Pattern.compile(regex));
        }
    }
}
