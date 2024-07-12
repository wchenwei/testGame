package com.hm.model.tank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 坦克精铸
 * @ClassName:  TankJingzhu   
 * @Description:
 * @author: zxj
 * @date:   2020年7月8日 上午11:10:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TankJingzhu {
	private Map<Integer, TankJingzhuWashBean> washMap = Maps.newHashMap();
	
	public boolean addWash(int position, TankJingzhuWashBean washBean) {
		washMap.put(position, washBean);
		return true;
	}
	
	public TankJingzhuWashBean getWahsBean(int position) {
		return washMap.getOrDefault(position, new TankJingzhuWashBean());
	}
	
	public boolean lock(int position, int index, int maxLockSize) {
		TankJingzhuWashBean tempWash = this.getWahsBean(position);
		boolean result = tempWash.lock(index, maxLockSize);
		if(result) {
			washMap.put(position, tempWash);
		}
		return result;
	}
	
	public boolean unlock(int position, int index, int maxLockSize) {
		TankJingzhuWashBean tempWash = this.getWahsBean(position);
		boolean result =  tempWash.unLock(index);
		if(result) {
			washMap.put(position, tempWash);
		}
		return result;
	}

	public int getLockSize(int position) {
		return this.getWahsBean(position).getLockSize();
	}

	public void addExp(long exp, int position, long maxExp) {
		TankJingzhuWashBean washBean = this.getWahsBean(position);
		washBean.addExp(exp, maxExp);
		this.washMap.put(position, washBean);
	}

	public List<TankJingzhuWashBean> getWash() {
		return Lists.newArrayList(this.washMap.values());
	}
}








