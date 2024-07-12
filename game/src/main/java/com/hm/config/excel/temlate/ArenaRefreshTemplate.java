package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("arena_refresh")
public class ArenaRefreshTemplate {
	private Integer rank_l;
	private Integer rank_r;
	private String pos_1;
	private String pos_2;
	private String pos_3;
	private String pos_4;

	public Integer getRank_l() {
		return rank_l;
	}

	public void setRank_l(Integer rank_l) {
		this.rank_l = rank_l;
	}
	public Integer getRank_r() {
		return rank_r;
	}

	public void setRank_r(Integer rank_r) {
		this.rank_r = rank_r;
	}
	public String getPos_1() {
		return pos_1;
	}

	public void setPos_1(String pos_1) {
		this.pos_1 = pos_1;
	}
	public String getPos_2() {
		return pos_2;
	}

	public void setPos_2(String pos_2) {
		this.pos_2 = pos_2;
	}
	public String getPos_3() {
		return pos_3;
	}

	public void setPos_3(String pos_3) {
		this.pos_3 = pos_3;
	}
	public String getPos_4() {
		return pos_4;
	}

	public void setPos_4(String pos_4) {
		this.pos_4 = pos_4;
	}
}
