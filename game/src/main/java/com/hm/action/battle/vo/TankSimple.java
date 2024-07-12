package com.hm.action.battle.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TankSimple {
	private int id;
	private long hp;
	private long mp;
	public TankSimple(int id) {
		super();
		this.id = id;
		this.hp = -1;
	}
	public boolean isDeath() {
		return hp==0;
	}
	

}
