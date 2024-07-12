package com.hm.model.activity;

import com.google.common.collect.Lists;
import com.hm.model.player.BasePlayer;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 等级区间标识
 * @author siyunlong  
 * @date 2019年2月13日 上午10:01:17 
 * @version V1.0
 */
@NoArgsConstructor
public class LevelSectionMark {
	public static class LevelSection{
		int min;
		int max;
		int value;
		public LevelSection(String temp) {
			String[] strs = temp.split(":");
			this.min = Integer.parseInt(strs[0]);
			this.max = Integer.parseInt(strs[1]);
			this.value = Integer.parseInt(strs[2]);
		}
		public LevelSection() {
			super();
		}
		
		public boolean isFit(int lv) {
			return lv >= min && lv <= max;
		}

		public boolean isFit(int lv, int id) {
			return lv >= min && lv <= max && value == id;
		}
	}
	private List<LevelSection> lvSectionList = Lists.newArrayList();
	
	public LevelSectionMark(String extend) {
		for (String temp : extend.split(",")) {
			this.lvSectionList.add(new LevelSection(temp));
		}
	}
	
	public int getValue(BasePlayer player) {
		int lv = player.playerLevel().getLv();
		LevelSection levelSection =  this.lvSectionList.stream().filter(e -> e.isFit(lv)).findFirst().orElse(null);
		return levelSection != null ? levelSection.value:1;
	}
	
	public List<Integer> getValues() {
		return this.lvSectionList.stream().map(e -> e.value).collect(Collectors.toList());
	}

	public boolean checkValueByLv(int lv, int id) {
		return this.lvSectionList.stream().anyMatch(e -> e.isFit(lv, id));
	}
}
