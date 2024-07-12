package com.hm.model.player;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CityCloneData {
	private int count;//克隆数量
	private long exp;//获得经验
	
	public void addCount(int add) {
		this.count += add;
	}
	
	public void addExp(long add) {
		this.exp += add;
	}
	
	public boolean isCanDel() {
		return count <= 0 && this.exp <= 0;
	}
}
