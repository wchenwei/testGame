package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("trump_vs")
public class TrumpVsTemplate {
	private Integer id;
	private Integer enemy_level;
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
	private Integer power;
	private Integer mission_map;
	private Integer soul_reward;
	private String reward;
	private Integer bottom_power;
	private String enemy_tank;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEnemy_level() {
		return enemy_level;
	}

	public void setEnemy_level(Integer enemy_level) {
		this.enemy_level = enemy_level;
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
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}
	public Integer getMission_map() {
		return mission_map;
	}

	public void setMission_map(Integer mission_map) {
		this.mission_map = mission_map;
	}
	public Integer getSoul_reward() {
		return soul_reward;
	}

	public void setSoul_reward(Integer soul_reward) {
		this.soul_reward = soul_reward;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getBottom_power() {
		return bottom_power;
	}

	public void setBottom_power(Integer bottom_power) {
		this.bottom_power = bottom_power;
	}
	public String getEnemy_tank() {
		return enemy_tank;
	}

	public void setEnemy_tank(String enemy_tank) {
		this.enemy_tank = enemy_tank;
	}
}
