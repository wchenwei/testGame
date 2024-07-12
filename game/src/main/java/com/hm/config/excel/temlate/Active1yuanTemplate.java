package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_1yuan")
public class Active1yuanTemplate {
	private Integer id;
	private Integer ground;
	private Integer day;
	private Integer ticket_num;
	private String recharge;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGround() {
		return ground;
	}

	public void setGround(Integer ground) {
		this.ground = ground;
	}
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getTicket_num() {
		return ticket_num;
	}

	public void setTicket_num(Integer ticket_num) {
		this.ticket_num = ticket_num;
	}
	public String getRecharge() {
		return recharge;
	}

	public void setRecharge(String recharge) {
		this.recharge = recharge;
	}
}
