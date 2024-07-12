package com.hm.libcore.util.lv;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @Description: 包含0级的等级代理
 * @author siyunlong  
 * @date 2019年7月25日 上午9:39:15 
 * @version V1.0
 */
public class LvAgentContain0 <T extends ILvValue>{
	//每一等级所需的总经验
	private long[] lvExps = null;
	private ImmutableMap<Integer,T> lvMap;
	private int maxLv;
	private int minLv;
	/**
	 * 构造等级经验结算代理
	 * @param lvs 等级经验列表
	 * @param lvTotalExp 每一等级是否是总经验
	 */
	public LvAgentContain0(List<T> lvs,boolean lvTotalExp) {
		this.minLv = lvs.stream().mapToInt(e -> e.getLv()).min().orElse(0);
		this.maxLv = lvs.stream().mapToInt(e -> e.getLv()).max().orElse(100);
		if(this.minLv == 0) {
			this.lvExps = new long[lvs.size()];
		}else{
			this.lvExps = new long[lvs.size()+1];
		}
		Map<Integer,T> lvMap = Maps.newHashMap();
		for (int i = 0; i < lvs.size(); i++) {
			T cuTemp = lvs.get(i);
			long totalExp = cuTemp.getExp();
			int index = cuTemp.getLv();
			if(!lvTotalExp && i > 0) {
				totalExp += lvExps[index-1];//加上上一等级经验
			}
			lvExps[index] = totalExp;
			lvMap.put(cuTemp.getLv(), cuTemp);
		}
		this.lvMap = ImmutableMap.copyOf(lvMap);
	}
	
	/**
	 * 计算最新等级
	 * @param oldLv 原等级
	 * @param totalExp 最新总经验
	 * @return
	 */
	public int getLevel(int oldLv,long totalExp) {
		oldLv = Math.max(oldLv, this.minLv);
		if(oldLv >= this.maxLv) {
			return this.maxLv;
		}
		while(true) {
			//比较下一等级
			if(totalExp >= this.lvExps[oldLv]) {
				if(++oldLv >= this.maxLv) {
					break;
				}
			}else {
				break;
			}
		}
		return Math.min(maxLv, oldLv);
	}
	
	public int getLevel(long totalExp) {
		return getLevel(this.minLv,totalExp);
	}
	
	
	/**
	 * 获取等级实体
	 * @param lv
	 * @return
	 */
	public T getLevelValue(int lv) {
		if(lv <= 0 || lv > this.maxLv) {
			return null;
		}
		return lvMap.get(lv);
	}

	/**
	 * 获取模块最大等级
	 * @return
	 */
	public int getMaxLv() {
		return maxLv;
	}
	
	public static void main(String[] args) {
		List<LvValueTest> list = Lists.newArrayList();
		for (int i = 1; i < 100; i++) {
			list.add(new LvValueTest(i, 100*(i)));
		}
		for (LvValueTest lvValueTest : list) {
			System.err.println(lvValueTest.getLv()+"="+lvValueTest.getExp());
		}
		LvAgentContain0<LvValueTest> lvAgent = new LvAgentContain0<>(list, true);
		System.err.println(lvAgent.getLevel(1));
	}
}
