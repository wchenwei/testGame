package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("revenge_level")
public class RevengeLevelTemplate {
	private Integer average_level_low;
	private Integer average_level_up;
	private Integer ghost_soldier;
	private Integer ghost_boss_shadow;
	private Integer ghost_boss;
	private Integer black_soldier;
	private Integer black_leader;

	public Integer getAverage_level_low() {
		return average_level_low;
	}

	public void setAverage_level_low(Integer average_level_low) {
		this.average_level_low = average_level_low;
	}
	public Integer getAverage_level_up() {
		return average_level_up;
	}

	public void setAverage_level_up(Integer average_level_up) {
		this.average_level_up = average_level_up;
	}
	public Integer getGhost_soldier() {
		return ghost_soldier;
	}

	public void setGhost_soldier(Integer ghost_soldier) {
		this.ghost_soldier = ghost_soldier;
	}
	public Integer getGhost_boss_shadow() {
		return ghost_boss_shadow;
	}

	public void setGhost_boss_shadow(Integer ghost_boss_shadow) {
		this.ghost_boss_shadow = ghost_boss_shadow;
	}
	public Integer getGhost_boss() {
		return ghost_boss;
	}

	public void setGhost_boss(Integer ghost_boss) {
		this.ghost_boss = ghost_boss;
	}
	public Integer getBlack_soldier() {
		return black_soldier;
	}

	public void setBlack_soldier(Integer black_soldier) {
		this.black_soldier = black_soldier;
	}
	public Integer getBlack_leader() {
		return black_leader;
	}

	public void setBlack_leader(Integer black_leader) {
		this.black_leader = black_leader;
	}
}
