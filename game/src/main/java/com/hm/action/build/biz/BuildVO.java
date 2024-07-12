package com.hm.action.build.biz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildVO {
	private int id;
	private int buildType;
	private int lv;
	private long cost;
	public BuildVO(int id,int lv,long cost) {
		this.id=id;
		this.lv=lv;
		this.cost = cost;
	}
	

}
