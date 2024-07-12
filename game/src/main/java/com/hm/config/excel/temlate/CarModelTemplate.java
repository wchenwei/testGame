package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("car_model")
public class CarModelTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String icon_name;
	private String icon_small;
	private String attr;
	private String unlock;
	private String csbPro;
	private Integer act_display;
	private String world_modle;
	private Float fight_zoom;
	private Integer active_display;
	private Integer frame_count;
	private Integer name_offset_x;
	private Integer name_offset_y;
	private Integer shadow;
	private Float scale;
	private Integer can_wait;
	private int paperId;

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
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon_name() {
		return icon_name;
	}

	public void setIcon_name(String icon_name) {
		this.icon_name = icon_name;
	}
	public String getIcon_small() {
		return icon_small;
	}

	public void setIcon_small(String icon_small) {
		this.icon_small = icon_small;
	}
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getUnlock() {
		return unlock;
	}

	public void setUnlock(String unlock) {
		this.unlock = unlock;
	}
	public String getCsbPro() {
		return csbPro;
	}

	public void setCsbPro(String csbPro) {
		this.csbPro = csbPro;
	}
	public Integer getAct_display() {
		return act_display;
	}

	public void setAct_display(Integer act_display) {
		this.act_display = act_display;
	}
	public String getWorld_modle() {
		return world_modle;
	}

	public void setWorld_modle(String world_modle) {
		this.world_modle = world_modle;
	}
	public Float getFight_zoom() {
		return fight_zoom;
	}

	public void setFight_zoom(Float fight_zoom) {
		this.fight_zoom = fight_zoom;
	}
	public Integer getActive_display() {
		return active_display;
	}

	public void setActive_display(Integer active_display) {
		this.active_display = active_display;
	}
	public Integer getFrame_count() {
		return frame_count;
	}

	public void setFrame_count(Integer frame_count) {
		this.frame_count = frame_count;
	}
	public Integer getName_offset_x() {
		return name_offset_x;
	}

	public void setName_offset_x(Integer name_offset_x) {
		this.name_offset_x = name_offset_x;
	}
	public Integer getName_offset_y() {
		return name_offset_y;
	}

	public void setName_offset_y(Integer name_offset_y) {
		this.name_offset_y = name_offset_y;
	}
	public Integer getShadow() {
		return shadow;
	}

	public void setShadow(Integer shadow) {
		this.shadow = shadow;
	}
	public Float getScale() {
		return scale;
	}

	public void setScale(Float scale) {
		this.scale = scale;
	}

	public Integer getCan_wait() {
		return can_wait;
	}

	public void setCan_wait(Integer can_wait) {
		this.can_wait = can_wait;
	}

	public int getPaperId() {
		return paperId;
	}
}
