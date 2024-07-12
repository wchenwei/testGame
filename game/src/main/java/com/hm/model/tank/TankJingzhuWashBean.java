package com.hm.model.tank;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 坦克精铸，属性信息
 * @ClassName:  TankJingzhuSkillBean   
 * @Description:
 * @author: zxj
 * @date:   2020年7月6日 下午7:34:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TankJingzhuWashBean {
	private long exp;
	//随机到的属性信息
	private int[] washIdsArr = new int[3];
	//锁定的属性信息
	private List<Integer> lockWashIds = Lists.newArrayList();
	
	//添加随机到的属性信息
	public boolean addWashIds(List<Integer> list) {
		for(int i=0; i<washIdsArr.length; i++) {
			if(lockWashIds.contains(i)) {
				continue;
			}
			//随机出来的数量，跟需要的数量不一致
			if(list.size()<=0) {
				return false;
			}
			washIdsArr[i]=list.get(0);
			list.remove(0);
		}
		return true;
	}
	//添加锁定信息
	public boolean lock(int washId, int maxLockSize) {
		if(lockWashIds.size()+1>=maxLockSize || lockWashIds.contains(washId)) {
			return false;
		}
		lockWashIds.add(washId);
		return true;
	}
	//添加锁定信息
	public boolean unLock(int index) {
		return lockWashIds.remove(new Integer(index));
	}
	public int getLockSize() {
		return lockWashIds.size();
	}
	//增加经验
	public void addExp(long addExp, long maxExp) {
		this.exp+=addExp;
		if(exp>maxExp) {
			this.exp= maxExp;
		}
	}
}












