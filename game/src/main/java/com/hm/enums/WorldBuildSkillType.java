package com.hm.enums;

import java.util.Arrays;

/**
 * 
 * @Description: 世界建筑技能类型
 * @author siyunlong  
 * @date 2019年10月24日 下午8:02:16 
 * @version V1.0
 */
public enum WorldBuildSkillType {
	NBomb(1,"核弹"),
	
	;
	
    private int type;
    private String desc;

    WorldBuildSkillType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
    
    public static WorldBuildSkillType getWorldBuildSkillType(int type) {
		return Arrays.stream(WorldBuildSkillType.values()).filter(t -> t.getType()==type).findFirst().orElse(null);
	}
}
