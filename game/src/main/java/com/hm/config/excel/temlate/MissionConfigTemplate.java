package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_config")
public class MissionConfigTemplate {
	private Integer id;
	private Integer type;
	private Integer fbid;
	private String icon;
	private String name;
	private Integer tank_num;
	private String troops;
	private Integer boss;
	private Float atk;
	private Float hp;
	private Float hit;
	private Float dodge;
	private Float crit;
	private Float armor;
	private Float pierce;
	private Float def;
	private Float critAtk;
	private Float tenacity;
	private Integer item_best;
	private String item_show;
	private Integer else_drop;
	private String now_drop;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getFbid() {
		return fbid;
	}

	public void setFbid(Integer fbid) {
		this.fbid = fbid;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getTank_num() {
		return tank_num;
	}

	public void setTank_num(Integer tank_num) {
		this.tank_num = tank_num;
	}
	public String getTroops() {
		return troops;
	}

	public void setTroops(String troops) {
		this.troops = troops;
	}
	public Integer getBoss() {
		return boss;
	}

	public void setBoss(Integer boss) {
		this.boss = boss;
	}
	public Float getAtk() {
		return atk;
	}

	public void setAtk(Float atk) {
		this.atk = atk;
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
	public Float getPierce() {
		return pierce;
	}

	public void setPierce(Float pierce) {
		this.pierce = pierce;
	}
	public Float getDef() {
		return def;
	}

	public void setDef(Float def) {
		this.def = def;
	}
	public Float getCritAtk() {
		return critAtk;
	}

	public void setCritAtk(Float critAtk) {
		this.critAtk = critAtk;
	}
	public Float getTenacity() {
		return tenacity;
	}

	public void setTenacity(Float tenacity) {
		this.tenacity = tenacity;
	}
	public Integer getItem_best() {
		return item_best;
	}

	public void setItem_best(Integer item_best) {
		this.item_best = item_best;
	}
	public String getItem_show() {
		return item_show;
	}

	public void setItem_show(String item_show) {
		this.item_show = item_show;
	}
	public Integer getElse_drop() {
		return else_drop;
	}

	public void setElse_drop(Integer else_drop) {
		this.else_drop = else_drop;
	}
	public String getNow_drop() {
		return now_drop;
	}

	public void setNow_drop(String now_drop) {
		this.now_drop = now_drop;
	}
}
