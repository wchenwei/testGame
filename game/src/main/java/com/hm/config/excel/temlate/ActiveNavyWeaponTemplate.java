package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_navy_weapon")
public class ActiveNavyWeaponTemplate {
	private Integer id;
	private Integer damage;
	private Integer speed;
	private Integer prepare;
	private Integer last;
	private Integer area_attack_lr;
	private Integer area_attack_ud;
	private String bullet_para;
	private Integer cd;
	private Integer damage_range;
	private Integer knock_bomb;
	private Integer reach_bomb;
	private String need_item;
	private Integer price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDamage() {
		return damage;
	}

	public void setDamage(Integer damage) {
		this.damage = damage;
	}
	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getPrepare() {
		return prepare;
	}

	public void setPrepare(Integer prepare) {
		this.prepare = prepare;
	}
	public Integer getLast() {
		return last;
	}

	public void setLast(Integer last) {
		this.last = last;
	}
	public Integer getArea_attack_lr() {
		return area_attack_lr;
	}

	public void setArea_attack_lr(Integer area_attack_lr) {
		this.area_attack_lr = area_attack_lr;
	}
	public Integer getArea_attack_ud() {
		return area_attack_ud;
	}

	public void setArea_attack_ud(Integer area_attack_ud) {
		this.area_attack_ud = area_attack_ud;
	}
	public String getBullet_para() {
		return bullet_para;
	}

	public void setBullet_para(String bullet_para) {
		this.bullet_para = bullet_para;
	}
	public Integer getCd() {
		return cd;
	}

	public void setCd(Integer cd) {
		this.cd = cd;
	}
	public Integer getDamage_range() {
		return damage_range;
	}

	public void setDamage_range(Integer damage_range) {
		this.damage_range = damage_range;
	}
	public Integer getKnock_bomb() {
		return knock_bomb;
	}

	public void setKnock_bomb(Integer knock_bomb) {
		this.knock_bomb = knock_bomb;
	}
	public Integer getReach_bomb() {
		return reach_bomb;
	}

	public String getNeed_item() {
		return need_item;
	}

	public void setNeed_item(String need_item) {
		this.need_item = need_item;
	}

	public void setReach_bomb(Integer reach_bomb) {
		this.reach_bomb = reach_bomb;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
}
