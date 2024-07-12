package com.hm.statistics;

public enum StatisticsType {
	/**
12~13点间登录次数
18~19点间登录次数
20~21点间登录次数
0~1点间登录次数

获得荣誉数		当天用户获得的荣誉数	
获得系统金砖数	当天用户所获得的系统金砖总数	
充值数		当天用户充值总额	
答题次数		每答一次题不管对错算一次	
闪电战得奖励次数			
经典战役得奖励次数	
绘制图纸次数
绘制图纸刷新次数
日常任务完成数
十连抽次数	
后勤部购买资源次数
复活总数		当天用户复活总次数	
挑战主线失败次数	当天用户挑战主线关卡失败的次数
使用金砖复活数	当天用户所使用的金砖复活总数	



当天所在阵营积分	就是每天统计用来算阵营结盟得积分

##聊天发言次数
##资源战战斗次数

			
	 **/

	Honer(1,"获得荣誉数"),
	Login1213(2, "12~13点间登录次数"),
	Login1819(3, "18~19点间登录次数"),
	Login2021(4, "20~21点间登录次数"),
	Login0001(5, "0~1点间登录次数"),

	SysGold(6,"当天用户所获得的系统金砖总数"),
	Recharge(7,"当天用户充值总额"),
	AnswerQuestion(8,"每答一次题不管对错算一次"),
	ExperimentBattle(9,"闪电战得奖励次数"),
	CommonBattle(10,"经典战役得奖励次数"),
	TankDraw(11,"绘制图纸次数"),
	TankDrawFre(12,"绘制图纸刷新次数"),
	Task_Reward(13,"日常任务完成数"),
	TankTechResearch(14,"十连抽次数"),
	Treasury_Collection(15,"后勤部购买资源次数"),
	TroopRevive(16,"当天用户复活总次数"),
	TroopReviveGold(17,"当天用户所使用的金砖复活总数	"),
	MainFbFail(18,"当天用户复活总次数"),
	PvpOneByOneLaunch(19,"城战偷袭次数"),
	CampScore(20,"城战偷袭次数"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private StatisticsType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
