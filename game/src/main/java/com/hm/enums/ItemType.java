/**  
 * Project Name:SLG_GameFuture.  
 * File Name:ItemType.java  
 * Package Name:com.hm.enums  
 * Date:2017年12月28日下午4:37:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.bag.service.AbstractBagService;
import com.hm.action.bag.service.BagService;
import com.hm.action.bag.service.PieceBagService;
import com.hm.model.item.Items;


/**  
 * 道具类型类型枚举
 * ClassName:ItemType <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月28日 下午4:37:27 <br/>  
 * @author   zigm  
 * @version  1.1  
 * @since    
 */
public enum ItemType {
	OTHER(0, "其他") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	CURRENCY(1,"资产") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	TANK(2,"战兽") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	PAPER(3,"战兽图纸") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	PIECE(4,"装备碎片") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return SpringUtil.getBean(PieceBagService.class);
		}
	},
	ITEM(6,"道具、兽魂、装备碎片、宝石") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return SpringUtil.getBean(BagService.class);
		}
	},
	EQUIP(7,"指挥官装备") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	STONE(8,"宝石") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	ICON(10,"玩家头像") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	HEAD_FRAME(11,"玩家头像框") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	TITLE(12,"称号") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	AGENT(13, "特工") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	PassengerPIECE(14, "乘员碎片") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Passenger(15, "乘员") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	RealGift(16, "实物") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	TankPhoto(17, "坦克照片") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	TankStrenTool(18, "强化工具") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Arms(19, "武器") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Naming(20, "冠名权") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Score(21, "积分类型(不属于道具，用于展示)") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Aircraft(22, "飞机") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Pendant(23, "挂件") {//占位，暂时不用
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	Element(24, "元件") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},
	StrengthStore(25, "力量配件") {
		@Override
		public AbstractBagService createAbstractBagService() {
			return null;
		}
	},

    KFWorldWar(99, "跨服世界大战道具") {
        @Override
        public AbstractBagService createAbstractBagService() {
            return null;
        }
    },
	;
	
	private ItemType(int type,  String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	private final int type;
	
	private final String desc;
	
	public abstract AbstractBagService createAbstractBagService();

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	public static ItemType getType(int type) {
		for (ItemType buildType : ItemType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

	public static boolean isRealGift(int type) {
		return type==ItemType.RealGift.getType()||type==ItemType.Naming.getType();
	}

	public Items createItems(int id,long count) {
		return new Items(id,count,this);
	}
}
  
