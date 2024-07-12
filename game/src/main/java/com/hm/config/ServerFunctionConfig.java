package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.serverData.ServerDataBiz;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ServerFunctionUnlockTemplate;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerFunction;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import java.util.ArrayList;
import java.util.List;

@Config
public class ServerFunctionConfig extends ExcleConfig{
	private List<ServerFunctionUnlockTemplate> functionList = Lists.newArrayList();
	@Override
	public void loadConfig() {
		this.functionList = ImmutableList.copyOf(JSONUtil.fromJson(getJson(ServerFunctionUnlockTemplate.class), new TypeReference<ArrayList<ServerFunctionUnlockTemplate>>(){}));
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ServerFunctionUnlockTemplate.class);
	}
	

	public void checkFunctionOpen(int serverId,int lv,int openCityId,int maxGuildLv) {
		try {
			ServerData serverDate = ServerDataManager.getIntance().getServerData(serverId);
			ServerFunction serverFunction = serverDate.getServerFunction();
			functionList.forEach(t -> {
				if(!serverFunction.isServerUnlock(t.getId()) && isUnlock(lv,openCityId,maxGuildLv, t)){
					serverFunction.unlock(t.getId());
					ObserverRouter.getInstance().notifyObservers(ObservableEnum.ServerFunction,null,serverId,t.getId());
				}
			});
			if(serverFunction.Changed()){
				ServerDataBiz serverDataBiz = SpringUtil.getBean(ServerDataBiz.class);
				serverDate.save();
				//推送功能开启
				serverDataBiz.broadServerFunctionChange(serverFunction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//功能是否能够解锁
	public boolean isUnlock(int lv,int openCity,int maxGuildLv,ServerFunctionUnlockTemplate template){
		if(template.getLevel()>0&&lv<template.getLevel()){
			return false;
		}
		if(template.getCity()>0&&openCity<template.getCity()){
			return false;
		}
		if(template.getGuild_lv()>0&&maxGuildLv<template.getGuild_lv()){
			return false;
		}
		return true;
	}
	public ServerFunctionUnlockTemplate getFunctionTemplate(int id){
		return functionList.stream().filter(t -> t.getId() ==id).findFirst().orElse(null);
	}
}
