package com.hm.model.worldtroop;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * @Description: 部队行进路线
 * @author siyunlong  
 * @date 2018年11月3日 上午3:34:23 
 * @version V1.0
 */
@Getter
@Setter
public class TroopWay extends TroopComponent{
	//行进的城市id路线
	private LinkedList<Integer> wayList = Lists.newLinkedList();
	//本次总移动时间
	private long moveSecond;
	//到达下个城市的时间
	private long nextArriveSecond;
	//部队速度
	private int troopSpeed = 5;
	
	private int startCityId;
    private long startCityTime;//当前城池开始移动时间
	
	public void removeWayFirst() {
		this.wayList.removeFirst();
		SetChanged();
	}
	public void setWayList(LinkedList<Integer> wayList) {
		this.wayList = wayList;
		SetChanged();
	}
	public void setMoveSecond(long moveSecond) {
		this.moveSecond = moveSecond;
		SetChanged();
	}
	public void setStartCityId(int startCityId) {
		this.startCityId = startCityId;
        this.startCityTime = System.currentTimeMillis();
	}
	
	public boolean checkTroopMove() {
		this.nextArriveSecond = Math.max(1, this.nextArriveSecond);
		this.moveSecond ++;
		SetChanged();
		return this.moveSecond >= this.nextArriveSecond;
	}
	
	public void updateNextArriveSecond(int second) {
		this.nextArriveSecond += second;
		SetChanged();
	}

    public long getStartCityTime() {
        return startCityTime;
    }

	public int getEndCityId() {
		try {
			if(CollUtil.isNotEmpty(wayList)) {
				return wayList.getLast();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void clear() {
		this.wayList.clear();
		this.moveSecond = 0;
		this.startCityId = 0;
		this.nextArriveSecond = 0;
        this.startCityTime = 0;
		SetChanged();
	}
	
}
