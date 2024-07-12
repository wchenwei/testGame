package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ShopConfig;
import com.hm.model.shop.*;

import java.util.Arrays;

public enum ShopType {
		RestricteShop(1,"杂货铺") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new RestricteShopValue(this);
			}
		},
		PartShop(2,"配件商店"){
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new DiscountShopValue(this);
			}
		},
		BoutiqueShop(4,"精品商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new NomalShopValue(this);
			}
		},
		GuildShop(5,"部落商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new RestricteShopValue(this);
			}
		},
		ExpeditionShop(6,"远征商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new RestricteShopValue(this);
			}
		},
		AreanShop(7,"竞技商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		TowerShop(8,"使命商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		BoutiqueNewShop(9,"新精品店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		EquipmentShop(10,"装备商店"){
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		Stone(11,"宝石商店"){
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		CrossServerResShop(13,"跨服资源商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		ResShop(14,"物资商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		LevelShop(20,"等级商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LevelShop(this);
			}
		},
		FullLevelShop(21,"满级商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		KFShop(22,"跨服商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new RestricteShopValue(this);
			}
		},
		PassengerShop(23,"乘员商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		MemorialShop(24,"纪念商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new RestricteShopValue(this);
			}
		},
		MemorialShowShop(25,"纪念商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new NomalShopValue(this);
			}
		},
		KfRankShop(26,"纪念商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		KfWorldWarShop(27,"世界商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		FishShop(28,"鱼商") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new RestricteShopValue(this);
			}
		},
		GoldShop(29,"金砖商店") {
			@Override
			public PlayerShopValue getPlayerShopValue() {
				return new LimitShopValue(this);
			}
		},
		;
		
		private ShopType(int type,String desc){
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
		public int getShopType() {
			ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
			return shopConfig.getShopTypeTemplate(type).getType();
		}
		public static ShopType getShopType(int type) {
			return Arrays.stream(ShopType.values()).filter(t -> t.getType()==type).findFirst().orElse(null);
		}
		public abstract PlayerShopValue getPlayerShopValue();
		
		
}
