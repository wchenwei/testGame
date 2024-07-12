package com.hm.model.serverpublic;

import com.google.common.collect.Lists;
import com.hm.enums.ServerFunctionType;

import java.util.List;

public class ServerFunction extends ServerPublicContext {
	public List<Integer> functions =Lists.newArrayList();
	
	public boolean isServerUnlock(int serverFunctionType){
		return this.functions.contains(serverFunctionType);
	}
	public void unlock(int type){
		if(!isServerUnlock(type)){
			this.functions.add(type);
			SetChanged();
		}
	}
	public void unlock(ServerFunctionType serverFunctionType){
		unlock(serverFunctionType.getType());
	}
	
	public List<Integer> getFunctions() {
		return functions;
	}
	public void addAllLock(List<Integer> functions) {
		for (int type : functions) {
			unlock(type);
		}
	}
}
