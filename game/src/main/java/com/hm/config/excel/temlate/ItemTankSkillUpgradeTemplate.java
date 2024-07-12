package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_tank_skill_upgrade")
public class ItemTankSkillUpgradeTemplate {
	private Integer id;
	private Integer skill_id;
	private Integer skill_type;
	private Integer skill_level;
	private String up_cost_money;
	private String up_cost_piece;
	private String return_cost;
	private Integer combat;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public Integer getSkill_type() {
		return skill_type;
	}

	public void setSkill_type(Integer skill_type) {
		this.skill_type = skill_type;
	}
	public Integer getSkill_level() {
		return skill_level;
	}

	public void setSkill_level(Integer skill_level) {
		this.skill_level = skill_level;
	}
	public String getUp_cost_money() {
		return up_cost_money;
	}

	public void setUp_cost_money(String up_cost_money) {
		this.up_cost_money = up_cost_money;
	}
	public String getUp_cost_piece() {
		return up_cost_piece;
	}

	public void setUp_cost_piece(String up_cost_piece) {
		this.up_cost_piece = up_cost_piece;
	}
	public String getReturn_cost() {
		return return_cost;
	}

	public void setReturn_cost(String return_cost) {
		this.return_cost = return_cost;
	}
	public Integer getCombat() {
		return combat;
	}

	public void setCombat(Integer combat) {
		this.combat = combat;
	}
}
