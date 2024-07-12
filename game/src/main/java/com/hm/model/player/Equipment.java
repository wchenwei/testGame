package com.hm.model.player;

import cn.hutool.core.util.ArrayUtil;
import com.hm.config.excel.templaextra.PlayerArmExtraTemplate;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class Equipment {
	private int id;//部位
	private int equId;//装备id 用来取品质
	private int quality;//品质
	private int strengthenLv;//强化等级
	private int[] stone = new int[]{0,0,0};//宝石
	
	
	public Equipment() {
		super();
	}
	public Equipment(int id) {
		super();
		this.id = id;
	}

	public void changeEui(PlayerArmExtraTemplate template) {
		this.equId = template.getId();
		this.quality = template.getQuality();
	}
	public void strengthen(int lv) {
		this.strengthenLv +=lv;
	}
	public void changeStone(int stoneId, int index) {
		this.stone[index] = stoneId;
	}

	//获取最低级宝石的位置
	public int findMineStoneIndex() {
		int minId = Arrays.stream(this.stone).min().orElse(0);
		return ArrayUtil.indexOf(this.stone,minId);
	}

	public List<Integer> getSortStoneIds() {
		return Arrays.stream(this.stone)
				.filter(e -> (e > 0))
				.sorted().boxed().collect(Collectors.toList());
	}

	public boolean isUnlock() {
		return this.equId > 0;
	}
}
