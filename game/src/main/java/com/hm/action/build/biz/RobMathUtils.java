package com.hm.action.build.biz;

import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.kf.kfbuild.CrystalHunterLog;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.AttributeType;
import com.hm.enums.CommonValueType;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RobMathUtils {
	/**
	 * 计算可掠夺上线
	 * @param player
	 */
	public static long calCanRobMine(Player player) {
//		long curVal = player.playerCurrency().get(CurrencyKind.Crystal);
//		long maxVal = (long)player.getPlayerDynamicData().getMaxResLimit(AttributeType.Crystal_Limit);
//		return Math.max(maxVal-curVal, 0);
		return Integer.MAX_VALUE;//取消可掠夺上线
	}
	
	/**
	 * 获取今日数量
	 * @param logList
	 * @return
	 */
	public static int getTodayNum(List<CrystalHunterLog> logList) {
		long beginTime = DateUtil.beginOfDay(new Date()).getTime();
		return (int)logList.stream().filter(e -> e.getTime() > beginTime).count();
	}
	
	/**
	 * 当前最大的水晶npc数量
	 * @return
	 */
	public static int getMaxHunterNum() {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int nowHour = DateUtil.thisHour(true);
		int oneNum = commValueConfig.getCommValue(CommonValueType.BuildHunterHour);
		int count = (int)Arrays.stream(commValueConfig.getCommonValueByInts(CommonValueType.BuildHunterHour))
						.filter(e -> nowHour >= e).count();
		return oneNum * count;
	}
	
	public static int getTodayMaxHunterNum() {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int oneNum = commValueConfig.getCommValue(CommonValueType.BuildHunterHour);
		int count = commValueConfig.getCommonValueByInts(CommonValueType.BuildHunterHour).length;
		return oneNum * count;
	}
	
	/**
	 * 计算可被抢的上线
	 * @param player
	 */
	public static long calBeRobMine(Player player) {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		double rate = commValueConfig.getDoubleCommValue(CommonValueType.BuildRobMaxRate);
		long curVal = player.playerCurrency().get(CurrencyKind.Crystal);
		long maxVal = (long)player.getPlayerDynamicData().getMaxResLimit(AttributeType.Crystal_Protect_Limit);
		return Math.max((long)((curVal-maxVal)*rate), 0);
	}
	
	/**
	 * 计算预期可掠夺资源
	 * @param robPlayer
	 * @param bePlayer
	 * @return
	 */
	public static long calExpectRobMine(Player robPlayer,Player bePlayer) {
		return Math.min(calCanRobMine(robPlayer), calBeRobMine(bePlayer));
	}
	
//	/**
//	 * 计算真实的抢夺资源
//	 * @param robPlayer
//	 * @param bePlayer
//	 * @param rate
//	 * @return
//	 */
//	public static long calTrueRobMine(Player robPlayer,Player bePlayer,double rate) {
//		return Math.min((long)(calCanRobMine(robPlayer)*rate), calBeRobMine(bePlayer));
//	}
}
