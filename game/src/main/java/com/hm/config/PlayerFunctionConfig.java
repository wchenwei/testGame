package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.FunctionUnlockTemplate;
import com.hm.config.excel.temlate.TaskMainTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Config
public class PlayerFunctionConfig extends ExcleConfig{
	private List<FunctionUnlockTemplate> functionList = Lists.newArrayList();

	@Resource
	private TaskConfig taskConfig;

	@Override
	public void loadConfig() {
		this.functionList = ImmutableList.copyOf(JSONUtil.fromJson(getJson(FunctionUnlockTemplate.class), new TypeReference<ArrayList<FunctionUnlockTemplate>>(){}));
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(FunctionUnlockTemplate.class);
	}
	

	public void checkFunctionOpen(Player player) {
		try {
			for (FunctionUnlockTemplate t : functionList) {
				if(!player.getPlayerFunction().isOpenFunction(t.getId()) && isUnlock(player, t)){
					player.getPlayerFunction().addOpenFunction(t.getId());
					player.notifyObservers(ObservableEnum.FunctionUnlock, t.getId());
					player.sendMsg(MessageComm.S2C_FunctionUnlock,t.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//功能是否能够解锁
	public boolean isUnlock(Player player,FunctionUnlockTemplate template){
		int lv = player.playerLevel().getLv();
		int fbId = player.playerMission().getFbId();
		int taskId = player.playerMainTask().getLastCompleteId();
		if(template.getLevel()>0 && lv<template.getLevel()){
			return false;
		}
		if(template.getMission()>0 && fbId<=template.getMission()){
			return false;
		}
		if (template.getTask_Id()>0){
			if (taskId <= 0){
				return false;
			}
			TaskMainTemplateImpl pTemp = taskConfig.getTaskMainTemplate(taskId);
			TaskMainTemplateImpl cTemp = taskConfig.getTaskMainTemplate(template.getTask_Id());
			if (pTemp == null || pTemp.getOrder() < cTemp.getOrder()){
				return false;
			}
		}
		return true;
	}
	public FunctionUnlockTemplate getFunctionTemplate(int id){
		return functionList.stream().filter(t -> t.getId() ==id).findFirst().orElse(null);
	}
}
