package com.hm.model.player;

public enum CurrencyKind {
		SysGold(1),//系统获得的金币
		Gold(2),//充值获得的金币
		N3(3),// 经验 废弃
		Cash(4),// 钞票
		Oil(5),// 石油
		N6(6),// 废弃
		Contr(7),//功勋
		Sports(8),//竞技币(龙币)
		Expedition(9),//远征币
		N10(10),
		N11(11),//荣誉
		MissionMedal(12),//使命勋章（爬塔币）
		Shipping(13),//航运币
		SilverBar(14),//银条(柏林宝库使用的货币)
		MineKfScore(15),//跨服资源战积分
		Material(16),//物资(物资商店使用的货币)
		N17(17),
		KfMoney(18),//跨服王者币
		ExpMoney(19),//经验币
		BhMoney(20),//兵魂币
		Crystal(21),//晶石
		JnMoney(22),//纪念币
		HolidayScore(23),//庆典积分
		KfRankScore(24),//跨服排位积分
		N25(25),//战令经验
		WorldWarScore(26),//世界大战争霸币
		N27(27),
		FishScore(28),//捕鱼积分
		;
		public final int Index;

		CurrencyKind(int index) {
			this.Index=index;
		}

		public int getIndex() {
			return Index;
		}
		
		public static CurrencyKind getCurrencyByIndex(int index){
			for (CurrencyKind kind : CurrencyKind.values()) {
				if(index == kind.getIndex()) return kind; 
			}
			return null;
		}
	}