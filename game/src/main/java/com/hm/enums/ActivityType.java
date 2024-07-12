package com.hm.enums;

import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.model.activity.display.BindPhoneActivity;
import com.hm.model.activity.introductionWeb.IntroductionWebActivity;
import com.hm.model.activity.kfSeasonShop.KfSeasonShopActivity;
import com.hm.model.activity.kfSeasonShop.KfSeasonShopValue;
import com.hm.model.activity.kfactivity.*;
import com.hm.model.activity.kfseason.KFSeasonActivity;
import com.hm.model.activity.kfworldwarshop.KfWorldWarShopActivity;
import com.hm.model.activity.kfworldwarshop.KfWorldWarShopValue;
import com.hm.model.activity.loginWelfare.LoginWelfareActivity;
import com.hm.model.activity.loginWelfare.PlayerLoginWelfareValue;
import com.hm.model.activity.mergeserver.MergeServerActivity;
import com.hm.model.activity.mergeserver.MergeServerValue;
import com.hm.model.activity.payeveryday.PayEveryDayActivity;
import com.hm.model.activity.payeveryday.PayEveryDayValue;
import com.hm.model.activity.qqprivilege.PrivilegeActivity;
import com.hm.model.activity.qqprivilege.PrivilegeValue;
import com.hm.model.activity.ranking.RankingActivity;
import com.hm.model.activity.threedays.PlayerThreeDayValue;
import com.hm.model.activity.threedays.ThreeDayActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ActivityType {

	LoginWelfare(3, "7日登录福利",1) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new LoginWelfareActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new PlayerLoginWelfareValue();
		}
	},
	Ranking(10, "冲榜活动", 2) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new RankingActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
    ThreeDay(11, "三日活动-新7日活动", 1) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new ThreeDayActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new PlayerThreeDayValue();
		}
	},
	MergeServer(36, "合服狂欢", 2) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new MergeServerActivity();
		}
		
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new MergeServerValue();
		}
	},
	BindPhone(38,"绑定手机号",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new BindPhoneActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	BuyEveryDay(45,"每日必买",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new PayEveryDayActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new PayEveryDayValue();
		}
	},
	KfExpeditionActivity(56, "跨服远征活动",1) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfExpeditionActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	KFScoreWar(66, "跨服积分战", 2) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfScoreActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	KFKingsCanyon(91,"跨服王者峡谷",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfKingsCanyonActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	IntroductionWeb(94, "游戏攻略页面",2) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new IntroductionWebActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	KFScuffle(102,"跨服乱斗",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfScuffleActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	KFSeason(104,"跨服赛季活动",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KFSeasonActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	KFSeasonShop(105,"跨服赛季商店",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfSeasonShopActivity();
		}
		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new KfSeasonShopValue();
		}
	},
	KfWorldWar(108, "跨服世界大战", 2) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfWorldWarActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return null;
		}
	},
	QQPrivilege(110, "QQ大厅特权", 2) {
		@Override
		public AbstractActivity getActivityTemplate() {
			return new PrivilegeActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new PrivilegeValue();
		}
	},
	KfWorldWarShop(112,"跨服世界大战商店",2){
		@Override
		public AbstractActivity getActivityTemplate() {
			return new KfWorldWarShopActivity();
		}

		@Override
		public PlayerActivityValue getPlayerActivityValue() {
			return new KfWorldWarShopValue();
		}
	},

    ;

	private ActivityType(int type, String desc, int openType) {
		this.type = type;
		this.desc = desc;
		this.openType = openType;
	}

	private int type;
	private String desc;
	// 开放类型,1-永久开发 2-后台开放
	private int openType;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public boolean isForeverType() {
		return openType == 1;
	}
	
	public static boolean isForeverType(int activityType) {
		ActivityType acType = getActivityType(activityType);
		return acType != null && acType.isForeverType();
	}

	public static ActivityType getActivityType(int type) {
		for (ActivityType buildType : ActivityType.values()) {
			if (buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

	/**
	 * 获取永久开放的活动
	 * @return
	 */
	public static List<ActivityType> getForeverActivity() {
		return Arrays.stream(ActivityType.values()).filter(e -> e.isForeverType()).collect(Collectors.toList());
	}

	public abstract AbstractActivity getActivityTemplate();

	public abstract PlayerActivityValue getPlayerActivityValue();

}
