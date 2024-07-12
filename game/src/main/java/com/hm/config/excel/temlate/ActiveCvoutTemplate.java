package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_cvout")
public class ActiveCvoutTemplate {
	private Integer id;
	private Integer stage;
	private Integer map_id;
	private Integer location;
	private Integer event_type;
	private String cost;
	private Integer exploit;
	private Integer reward;
	private Integer recharge_gift_group;
	private Integer circle_id;
	private String cost_gold_extra;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getMap_id() {
		return map_id;
	}

	public void setMap_id(Integer map_id) {
		this.map_id = map_id;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getEvent_type() {
		return event_type;
	}

	public void setEvent_type(Integer event_type) {
		this.event_type = event_type;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getExploit() {
		return exploit;
	}

	public void setExploit(Integer exploit) {
		this.exploit = exploit;
	}
	public Integer getReward() {
		return reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}
	public Integer getRecharge_gift_group() {
		return recharge_gift_group;
	}

	public void setRecharge_gift_group(Integer recharge_gift_group) {
		this.recharge_gift_group = recharge_gift_group;
	}
	public Integer getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(Integer circle_id) {
		this.circle_id = circle_id;
	}

	public String getCost_gold_extra() {
		return cost_gold_extra;
	}

	public void setCost_gold_extra(String cost_gold_extra) {
		this.cost_gold_extra = cost_gold_extra;
	}
}
