package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("npc_tank")
public class NpcTankTemplate {
	private Integer id;
	private String name;
	private Integer model;
	private Integer type;
	private Integer level;
	private Integer reform;
	private String skill_level;
	private Integer atk;
	private Integer def;
	private Integer hp;
	private Integer hit;
	private Integer dodge;
	private Integer crit;
	private Integer crit_def;
	private Float crit_dam;
	private Float crit_res;
	private Float damage_increase;
	private Float damage_reduce;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getReform() {
		return reform;
	}

	public void setReform(Integer reform) {
		this.reform = reform;
	}
	public String getSkill_level() {
		return skill_level;
	}

	public void setSkill_level(String skill_level) {
		this.skill_level = skill_level;
	}
	public Integer getAtk() {
		return atk;
	}

	public void setAtk(Integer atk) {
		this.atk = atk;
	}
	public Integer getDef() {
		return def;
	}

	public void setDef(Integer def) {
		this.def = def;
	}
	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}
	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer hit) {
		this.hit = hit;
	}
	public Integer getDodge() {
		return dodge;
	}

	public void setDodge(Integer dodge) {
		this.dodge = dodge;
	}
	public Integer getCrit() {
		return crit;
	}

	public void setCrit(Integer crit) {
		this.crit = crit;
	}
	public Integer getCrit_def() {
		return crit_def;
	}

	public void setCrit_def(Integer crit_def) {
		this.crit_def = crit_def;
	}
	public Float getCrit_dam() {
		return crit_dam;
	}

	public void setCrit_dam(Float crit_dam) {
		this.crit_dam = crit_dam;
	}
	public Float getCrit_res() {
		return crit_res;
	}

	public void setCrit_res(Float crit_res) {
		this.crit_res = crit_res;
	}
	public Float getDamage_increase() {
		return damage_increase;
	}

	public void setDamage_increase(Float damage_increase) {
		this.damage_increase = damage_increase;
	}
	public Float getDamage_reduce() {
		return damage_reduce;
	}

	public void setDamage_reduce(Float damage_reduce) {
		this.damage_reduce = damage_reduce;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
