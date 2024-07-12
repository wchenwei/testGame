package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.TankBaseTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.NpcPvpTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.war.sg.setting.TankSetting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@FileConfig("npc_pvp")
public class PvpNpcTemplate extends NpcPvpTemplate{

	@Getter
	private Map<Integer, Integer> skillMap = Maps.newHashMap();

	private List<Items> dropItems;
	
	public void init(int[] unlockSkillLvs, TankBaseTemplate tankTemplate) {
		if (StrUtil.isNotEmpty(tankTemplate.getSkill_id())){
			Integer lv = getLevel();
			int[] skills = StringUtil.splitStr2IntArray(tankTemplate.getSkill_id(), ",");
			for (int i = 0; i < unlockSkillLvs.length; i++) {
				int unlockLv = unlockSkillLvs[i];
				if (skills.length > i && lv >= unlockLv){
					skillMap.put(skills[i], lv);
				}
			}
		}
		if(StrUtil.isNotEmpty(getReward())) {
			this.dropItems = ItemUtils.str2ItemList(getReward(), ",", ":");
		}
	}
	
	public List<Items> getDropItems() {
		return dropItems;
	}

	
	//坦克属性初始配表值
	public Map<Integer,Double> getNpcAttrInit(TankSetting tankSetting) {
		Map<Integer,Double> attrMap = Maps.newConcurrentMap();
		attrMap.put(TankAttrType.ATK.getType(), (double)getAtk());
		attrMap.put(TankAttrType.DEF.getType(), (double)getDef());
		attrMap.put(TankAttrType.HP.getType(), (double)getHp());
		attrMap.put(TankAttrType.DODGE.getType(), (double)getDodge());
		attrMap.put(TankAttrType.HIT.getType(), (double)getHit());
		attrMap.put(TankAttrType.CRIT.getType(), (double)getCrit());
		attrMap.put(TankAttrType.CritDef.getType(), (double)getCrit_def());
		attrMap.put(TankAttrType.CritDamPer.getType(), (double)getCrit_dam());
		attrMap.put(TankAttrType.CritResPer.getType(), (double)getCrit_res());
		attrMap.put(TankAttrType.AtkCd.getType(), (double)tankSetting.getAtk_cd());
		attrMap.put(TankAttrType.FirstAtkCd.getType(), (double)tankSetting.getFist_atk());
		attrMap.put(TankAttrType.AddAtkPer.getType(), (double)getDamage_increase());
		attrMap.put(TankAttrType.ReduceAtkPer.getType(), (double)getDamage_reduce());
		return attrMap;
	}
}
