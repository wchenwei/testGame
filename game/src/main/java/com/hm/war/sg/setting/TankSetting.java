package com.hm.war.sg.setting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankBaseTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.AtkChoiceEnum;

import java.util.List;
import java.util.Map;

@FileConfig("tank_base")
public class TankSetting extends TankBaseTemplate{
	private AtkChoiceEnum atkChoiceEnum;
	private List<Integer> skillList;
	private List<Integer> techChipList;
	private List<Integer> techSkillList;
	private List<Integer> specila = Lists.newArrayList();
	private List<Integer> magicSkillIds = Lists.newArrayList();
	private List<Integer> magicSuperSkillIds = Lists.newArrayList();
	private Map<Integer,Integer> repaceSkillMap = Maps.newHashMap();//替换技能map
	private List<Integer> terrainSkillList = Lists.newArrayList();
	
	public void init() {
		this.atkChoiceEnum = AtkChoiceEnum.getTargetChoiceEnum(getAtk_type());
		this.skillList = StringUtil.splitStr2IntegerList(getSkill_id(), ",");
		this.techChipList = StringUtil.splitStr2IntegerList(this.getTec_tree(), ",");
		this.techSkillList = StringUtil.splitStr2IntegerList(this.getTec_skill(), ",");
		this.specila = StringUtil.splitStr2IntegerList(this.getTank_speciality(), ",");

		this.magicSkillIds = StringUtil.splitStr2IntegerList(this.getMagic_reform_skill(), ":");
        this.magicSuperSkillIds = StringUtil.splitStr2IntegerList(this.getMagic_reform_super_skill(), ":");
		this.repaceSkillMap = StringUtil.strToMap(getTec_skill_replace(), ";", ",");
		this.terrainSkillList = StringUtil.splitStr2IntegerList(this.getTerrain_skill(), ",");
	}

	public AtkChoiceEnum getAtkChoiceEnum() {
		return atkChoiceEnum;
	}

	public List<Integer> getSkillList() {
		return skillList;
	}

	//根据坦克星级获取坦克科技的技能
	public int getStarSkill(int position) {
		if(position>this.techSkillList.size()) {
			return 0;
		}
		return this.techSkillList.get(position-1);
	}

	public boolean containsSpecial(int type) {
		return specila.contains(type);
	}
	
	public List<Integer> getMagicSkillIds() {
		return Lists.newArrayList(magicSkillIds);
	}
	public List<Integer> getSuperMagicSkillIds(){
	    return Lists.newArrayList(magicSuperSkillIds);
    }

    public List<Integer> getAllSkillIds(){
        List<Integer> ids = getMagicSkillIds();
        ids.addAll(getSuperMagicSkillIds());
	    return ids;
    }

	
	//坦克属性初始配表值
	public Map<TankAttrType,Double> getTankAttrInit() {
		Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
		attrMap.put(TankAttrType.ATK, (double)getAtk());
		attrMap.put(TankAttrType.DEF, (double)getDef());
		attrMap.put(TankAttrType.HP, (double)getHp());
		attrMap.put(TankAttrType.DODGE, (double)getDodge());
		attrMap.put(TankAttrType.HIT, (double)getHit());
		attrMap.put(TankAttrType.CRIT, (double)getCrit());
		attrMap.put(TankAttrType.CritDef, (double)getCrit_def());
		attrMap.put(TankAttrType.CritDamPer, (double)getCrit_dam());
		attrMap.put(TankAttrType.CritResPer, (double)getCrit_res());
		
		attrMap.put(TankAttrType.AtkCd, (double)getAtk_cd());
		attrMap.put(TankAttrType.FirstAtkCd, (double)getFist_atk());
		return attrMap;
	}

	public Map<Integer, Integer> getRepaceSkillMap() {
		return repaceSkillMap;
	}

	public List<Integer> getTerrainSkillList() {
		return terrainSkillList;
	}

	public List<Integer> getTechSkillList() {
		return techSkillList;
	}
	
}
