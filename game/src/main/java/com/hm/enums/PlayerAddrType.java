package com.hm.enums;

import com.hm.redis.type.RedisTypeEnum;

/**
 *
 * @Description: 收货地址类型
 * @author xjt  
 * @date 2019年11月12日14:57:28 
 * @version V1.0
 */
public enum PlayerAddrType {
	Eggr(1,"砸蛋活动") {
		@Override
		public RedisTypeEnum getRedisTypeEnum() {
			return RedisTypeEnum.EggTrueItems;
		}
	},
	RechargeCarnival(2,"充值狂欢") {
		@Override
		public RedisTypeEnum getRedisTypeEnum() {
			return RedisTypeEnum.RechargeCarnivalAddr;
		}
	},
	Anniversary(3,"周年活动") {
		@Override
		public RedisTypeEnum getRedisTypeEnum() {
			return RedisTypeEnum.AnniversaryAddr;
		}
	},
	Act97(4,"6周年活动") {
		@Override
		public RedisTypeEnum getRedisTypeEnum() {
			return RedisTypeEnum.Act97Recharge;
		}
	},
	Act121(5,"新版国庆") {
		@Override
		public RedisTypeEnum getRedisTypeEnum() {
			return RedisTypeEnum.Act121Recharge;
		}
	},
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private PlayerAddrType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private String desc;

	public abstract RedisTypeEnum getRedisTypeEnum();
	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
