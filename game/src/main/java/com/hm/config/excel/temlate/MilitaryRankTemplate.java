package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("military_rank")
public class MilitaryRankTemplate {
	private Integer id;
	private String icon_b;
	private String icon_l;
	private String name;
	private String dec;
	private Integer atk_add;
	private String exp;
	private Integer fight_score;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon_b() {
		return icon_b;
	}

	public void setIcon_b(String icon_b) {
		this.icon_b = icon_b;
	}
	public String getIcon_l() {
		return icon_l;
	}

	public void setIcon_l(String icon_l) {
		this.icon_l = icon_l;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public Integer getAtk_add() {
		return atk_add;
	}

	public void setAtk_add(Integer atk_add) {
		this.atk_add = atk_add;
	}
	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}
	public Integer getFight_score() {
		return fight_score;
	}

	public void setFight_score(Integer fight_score) {
		this.fight_score = fight_score;
	}
}
