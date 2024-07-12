package com.hm.enums;

/**
 * 
 * @Description: 城镇状态
 * @author siyunlong  
 * @date 2019年1月17日 上午10:23:34 
 * @version V1.0
 */
public enum CityStatusType {
	Roadblock(1,"路障"),
	NoAirdrop(2,"禁止空头"),
    Protect(3, "保护罩"),
    ArtillerySupport(4, "炮火支援"),
    ;

    private int type;
    private String desc;

    CityStatusType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
