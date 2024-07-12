package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerHead extends PlayerDataContext {
	private String icon;//当前头像
	private int frameIcon =1 ;//当前头像框
	private ArrayList<Integer> unlockIcons = Lists.newArrayList();//解锁的头像
	private ArrayList<Integer> unlockFreamIcon =Lists.newArrayList();//解锁的头像框
	// 带时效 的头像框
	private Map<Integer, Long> iconAndTimeMap = Maps.newConcurrentMap();
	
	public String getIcon() {
		return icon;
	}

	public void changeIcon(int icon) {
		this.icon=icon+"";
		SetChanged();
	}
	public int getFrameIcon() {
		return frameIcon;
	}
	public void changeHeadFrameIcon(int headFrameIcon) {
		if(isUnlockFrameIcon(headFrameIcon)){
			this.frameIcon=headFrameIcon;
			SetChanged();
		}
	}
	//是否已经解锁该头像
	public boolean isUnlockIcon(int icon){
		this.checkHead();
		if(this.unlockIcons.contains(icon)){
			return true;
		}
		return this.iconAndTimeMap.containsKey(icon);
	}
	//是否解锁该头像框
	public boolean isUnlockFrameIcon(int icon){
		return this.unlockFreamIcon.contains(icon);
	}
	//解锁头像
	public void unlockIcon(int icon){
		if(!isUnlockIcon(icon)){
			this.unlockIcons.add(icon);
			SetChanged();
		}
	}
	public void unlockIcon(int icon, long endTime){
		this.iconAndTimeMap.put(icon, endTime);
		SetChanged();
	}

	//解锁头像框
	public void unlockFrameIcon(int icon){
		if(!isUnlockFrameIcon(icon)){
			this.unlockFreamIcon.add(icon);
			SetChanged();
		}
	}

	/**
	 *检查头像是否过期
	 */
	public void checkHead() {
		if(CollUtil.isEmpty(iconAndTimeMap)){
			return;
		}
		if(iconAndTimeMap.containsKey(this.icon) && iconAndTimeMap.get(this.icon) < System.currentTimeMillis()){
			// 该头像失效了，使用默认头像
			changeIcon(GameConstants.DEFAULT_ICON_ID);
		}
		this.iconAndTimeMap = iconAndTimeMap.entrySet().stream()
				.filter(entry -> System.currentTimeMillis() < entry.getValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		SetChanged();
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerHead", this);
	}

}
