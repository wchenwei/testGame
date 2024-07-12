package com.hm.model.tank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.MagicSkillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TankMagicReform {
	private int reformLv;//魔改等级
	private Map<Integer,Integer> skills = Maps.newConcurrentMap();
	private int bigSkillId;//魔改大招
    private int superBigSkillId;//魔改二阶技能
	public void reform(int skill, MagicSkillType magicSkillType) {
		this.reformLv++;
		switch(magicSkillType){
            case Normal:
                CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
                int lvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformSkillLimit);
                int oldLv = skills.getOrDefault(skill, 0);
                this.skills.put(skill, Math.min(lvLimit,oldLv+1));
                break;
            case BigSkill:
                this.bigSkillId = skill;
                break;
            case SuperBigSkill:
                this.superBigSkillId = skill;
                break;

        }
	}
	//获取满级技能
	public List<Integer> getFullSkillIds(){
		List<Integer> fullSkillIds = Lists.newArrayList();
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int lvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformSkillLimit);
		skills.forEach((key,value)-> {
			if(value>=lvLimit) fullSkillIds.add(key);
			} );
		return fullSkillIds;
	}
	public int getSkillLv(int skillId) {
		return skills.getOrDefault(skillId, 0);
	}
}
