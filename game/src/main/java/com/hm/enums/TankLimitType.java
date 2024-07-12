package com.hm.enums;

import com.hm.war.sg.setting.TankSetting;

import java.util.Arrays;
import java.util.Optional;

/**
 * 
 * @Description: 坦克出战类型限制
 * @author xjt  
 * @date 2019年12月6日11:59:10
 * @version V1.0
 */
public enum TankLimitType {
	TankTypeProhibit(1,"坦克类型") {
		@Override
		public int getParam(TankSetting tankSetting) {
			return tankSetting.getType();
		}
	},
	TankCampProhibit(2,"坦克阵营") {
		@Override
		public int getParam(TankSetting tankSetting) {
			return tankSetting.getCountry();
		}
	},
	TankRareProhibit(3,"坦克资质") {
		@Override
		public int getParam(TankSetting tankSetting) {
			return tankSetting.getRare();
		}
	},
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private TankLimitType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public abstract int getParam(TankSetting tankSetting);
	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public static TankLimitType getTankLimitType(int type){
		Optional<TankLimitType> optional = Arrays.stream(TankLimitType.values()).filter(t ->t.getType()==type).findFirst();
		if(!optional.isPresent()){
			return null;
		}
		return optional.get();
	}
}
