package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_chirst_wish")
public class ActiveChirstWishTemplate {
	private Integer id;
	private Integer last_wish;
	private Integer next_wish;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLast_wish() {
		return last_wish;
	}

	public void setLast_wish(Integer last_wish) {
		this.last_wish = last_wish;
	}
	public Integer getNext_wish() {
		return next_wish;
	}

	public void setNext_wish(Integer next_wish) {
		this.next_wish = next_wish;
	}
}
