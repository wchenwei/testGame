package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.model.item.Items;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResBackMode {
	private int type;//模块类型
	private int count;//可找回次数
	private List<Items> rewards =Lists.newArrayList();//找回的单次奖励
	public ResBackMode(int type, int count) {
		super();
		this.type = type;
		this.count = count;
	}
	
	
}
