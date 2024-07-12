package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("base_monster")
public class BaseMonsterTemplate {
	private Integer id;
	private Integer tank_id;
	private Integer level;
	private String name;
	private String icon_monster;
	private Integer quality;
	private Integer tank_type;
	private Integer rows;
	private Integer hp_real;
	private Float atk;
	private Float def;
	private Float hp;
	private Float hit;
	private Float dodge;
	private Float crit;
	private Float armor;
	private Float crit_rate;
	private Float atk1;
	private Float def1;
	private Float atk2;
	private Float def2;
	private Integer skill_id;
	private Float skil_rate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon_monster() {
		return icon_monster;
	}

	public void setIcon_monster(String icon_monster) {
		this.icon_monster = icon_monster;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getTank_type() {
		return tank_type;
	}

	public void setTank_type(Integer tank_type) {
		this.tank_type = tank_type;
	}
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getHp_real() {
		return hp_real;
	}

	public void setHp_real(Integer hp_real) {
		this.hp_real = hp_real;
	}
	public Float getAtk() {
		return atk;
	}

	public void setAtk(Float atk) {
		this.atk = atk;
	}
	public Float getDef() {
		return def;
	}

	public void setDef(Float def) {
		this.def = def;
	}
	public Float getHp() {
		return hp;
	}

	public void setHp(Float hp) {
		this.hp = hp;
	}
	public Float getHit() {
		return hit;
	}

	public void setHit(Float hit) {
		this.hit = hit;
	}
	public Float getDodge() {
		return dodge;
	}

	public void setDodge(Float dodge) {
		this.dodge = dodge;
	}
	public Float getCrit() {
		return crit;
	}

	public void setCrit(Float crit) {
		this.crit = crit;
	}
	public Float getArmor() {
		return armor;
	}

	public void setArmor(Float armor) {
		this.armor = armor;
	}
	public Float getCrit_rate() {
		return crit_rate;
	}

	public void setCrit_rate(Float crit_rate) {
		this.crit_rate = crit_rate;
	}
	public Float getAtk1() {
		return atk1;
	}

	public void setAtk1(Float atk1) {
		this.atk1 = atk1;
	}
	public Float getDef1() {
		return def1;
	}

	public void setDef1(Float def1) {
		this.def1 = def1;
	}
	public Float getAtk2() {
		return atk2;
	}

	public void setAtk2(Float atk2) {
		this.atk2 = atk2;
	}
	public Float getDef2() {
		return def2;
	}

	public void setDef2(Float def2) {
		this.def2 = def2;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public Float getSkil_rate() {
		return skil_rate;
	}

	public void setSkil_rate(Float skil_rate) {
		this.skil_rate = skil_rate;
	}
}
