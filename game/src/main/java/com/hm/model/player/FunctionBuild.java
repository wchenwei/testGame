package com.hm.model.player;

import com.google.common.collect.Lists;

import java.util.List;

public class FunctionBuild extends PlayerDataContext {
	private List<Integer> ids = Lists.newArrayList();

	public void unlockBuild(int id){
		ids.add(id);
		SetChanged();
	}
}
