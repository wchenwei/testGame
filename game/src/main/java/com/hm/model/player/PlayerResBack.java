package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.action.resBack.biz.ResBackBiz;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.ResBackType;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * 资源找回
 * @author xjt
 *
 */
public class PlayerResBack extends PlayerDataContext {
	private ConcurrentHashMap<ResBackType, ResBackMode> map = new ConcurrentHashMap<>();
	//已经找回的模块
	private List<Integer> backTypes = Lists.newArrayList();
	public boolean isReceive(ResBackType resBackType) {
		return !backTypes.contains(resBackType.getType());
	}
	public ResBackMode getResBackMode(ResBackType resBackType){
		return map.get(resBackType);
	}
	public void resBack(int id) {
		this.backTypes.add(id);
		SetChanged();
	}
	public void resBackAll(List<ResBackType> resBackTypes) {
		List<Integer> types = resBackTypes.stream().map(t ->t.getType()).collect(Collectors.toList());
		this.backTypes.addAll(types);
		SetChanged();
	}
	//获取可以找回的模块
	public List<ResBackType> getBackModes(){
		return map.keySet().stream().filter(t -> !backTypes.contains(t.getType())).collect(Collectors.toList());
	}
	
	public void resetDay(){
		BasePlayer player = super.Context();
		this.map.clear();
		this.backTypes.clear();
		//注册第一天
		if(TimeUtils.isSameDay(player.playerBaseInfo().getCreateDate(), new Date())){
			return;
		}
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.ResBack)){
			return;
		}
		ResBackBiz resBackBiz = SpringUtil.getBean(ResBackBiz.class);
		this.map.putAll(resBackBiz.createActivityMode(player));

		//开启国战才能找回
//		if(player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WorldTroop)){
//			this.map.putAll(resBackBiz.createHonorMode(player));
//		}
		SetChanged();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerResBack", this);
	}
	
	
	
}
