package com.hm.action.guildwar;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Range;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.config.GameConstants;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerOpenData;

import java.util.Date;

public class WarConstants {
    public static boolean WarAttrStayFrame = true;
    public static int WarMoveFrame = 30;
    public static int WarCalFrame = 10;
	public static int FrameTime = 100;


	public static final Range<Integer> war3 = Range.closedOpen(1200,1260);//开启
	
	/**
	 * 获取国战index  0-上午 1-下午
	 * @return
	 */
	public static int getWarIndex() {
//		int curMinute = DateUtil.thisHour(true)*60+DateUtil.thisMinute();
//		return curMinute >= war1.lowerEndpoint() && curMinute < war3.lowerEndpoint() ? 0:1;
		return 1;
	}

	public static long getNextTime(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		ServerOpenData serverOpenData =	serverData.getServerOpenData();
		long startDayTime = DateUtil.beginOfDay(new Date()).getTime();
		int nextMinute = getTempMinute();
		if(serverOpenData.getOpenDay() < 2 && nextMinute < 1440) {
			startDayTime += GameConstants.DAY;//多加一天
		}
		return startDayTime+nextMinute*GameConstants.MINUTE;
	}

	
	private static int getTempMinute() {
		int curMinute = DateUtil.thisHour(true)*60+DateUtil.thisMinute();
		if(war3.contains(curMinute)) {
			return war3.upperEndpoint();
		}
		if(curMinute > war3.lowerEndpoint()) {
			return war3.lowerEndpoint() + 1440;//明天
		}
		return war3.lowerEndpoint();
	}

	/**
	 * 是否是城池争夺战国战进行中
	 * @param entity
	 * @return
	 */
	public static boolean isCityFightRunning(DBEntity entity) {
//		ServerWarData serverWarData = ServerDataManager.getIntance().getServerData(entity.getServerId()).getServerWarData();
//		return  !serverWarData.isOver() && serverWarData.getGuildWarType() == GuildWarType.CityFight;
		return false;
	}

	public static boolean isWarTime(int serverId) {
		return true;
	}

	
	public static void main(String[] args) {
		System.err.println(20*60);
	}
}
