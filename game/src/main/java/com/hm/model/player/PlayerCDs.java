package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.CDType;
import com.hm.model.cd.CdData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家cd数据-收矿,城防,聚宝盆
 * @author siyunlong  
 * @date 2018年3月19日 上午9:56:14 
 * @version V1.0
 */
public class PlayerCDs extends PlayerDataContext{
	//玩家cd
	private ConcurrentHashMap<Integer, CdData> cdMap = new ConcurrentHashMap<>(); 
	
	@Override
	public void initData() {
		super.initData();
		if(cdMap.size() < CDType.values().length) {//初始化
			for (CDType cdType : CDType.values()) {
				if(!cdMap.containsKey(cdType.getType())) {
					CdData data = new CdData(cdType);
					cdMap.put(cdType.getType(), data);
				}
			}
			SetChanged();
		}
	}
	
	//重置每日cd
	public void resetDayCd() {
		for (CDType cdType : CDType.values()) {
			if(cdType.isDayReset()) {
				CdData cdData = getCdDataByCdType(cdType);
				if(cdData!=null){
					cdData.resetCD(super.Context());
					SetChanged();
				}
			}
		}
	}

	public void resetCd(CDType cdType) {
		CdData cdData = getCdDataByCdType(cdType);
		if(cdData!=null){
			cdData.resetCD(super.Context());
			SetChanged();
		}
	}

	//获取采集数据
	public CdData getCdDataByCdType(CDType type){
		return cdMap.get(type.getType());
	}
	
	/**
	 * 强制增加cd数量
	 * @param type cd类型
	 * @param num 增加数量
	 */
	public void addCdNum(CDType type,long num) {
		if(num <= 0) {
			return;
		}
		CdData cdData = getCdDataByCdType(type);
		cdData.addCount(num);
		SetChanged();
	}
	
	/**
	 * 登录检查所有cd状态
	 */
	public void checkPlayerCd(){
		for (CdData cdData : cdMap.values()) {
			checkPlayerCd(cdData);
		}
	}
	public void checkPlayerCd(CDType type){
		CdData cdData = getCdDataByCdType(type);
		checkPlayerCd(cdData);
	}
	private void checkPlayerCd(CdData cdData){
		if(cdData!=null && cdData.checkCD(super.Context())){
			SetChanged();
		}
	}
	
	/**
	 * 触发cd事件,cd次数减一
	 * @param type
	 */
	public boolean touchCdEvent(CDType type) {
		return touchCdEvent(type,0);
	}
	/**
	 * 触发cd事件
	 * @param touchType=1则将cd 次数全部清除 ,不等于1则每次减1
	 * @return
	 */
	public boolean touchCdEvent(CDType type,int touchType){
		CdData cdData = getCdDataByCdType(type);
		if(cdData != null) {
			int reduceCount = touchType==1?cdData.getCount():1;
			cdData.touchCdEvent(super.Context(), reduceCount);
			SetChanged();
			return true;
		}
		return false;
	}
	
	/**
	 * 检查此cd是否能触发
	 * @param type
	 * @return
	 */
	public boolean checkCanCd(CDType type) {
		CdData cdData = getCdDataByCdType(type);
		if(cdData != null) {
			boolean isCan = cdData.getCount()>0;
			if(!isCan) {
				checkPlayerCd(type);//再次检查
				isCan = cdData.getCount()>0;
			}
			return isCan;
		}
		return false;
	}
	
	/**
	 * 获取距离下次触发cd还剩余的秒数
	 * @param type
	 * @return
	 */
	public int getNextCdSecond(CDType type) {
		CdData cdData = getCdDataByCdType(type);
		if(cdData != null) {
			return cdData.getNextCdSecond();
		}
		return 0;
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		List<CdData> cdList = Lists.newArrayList(cdMap.values());
		for (CdData cd : cdList) {
			cd.calNextCdSecond();
		}
		msg.addProperty("PlayerCDs", cdList);
	}
}
