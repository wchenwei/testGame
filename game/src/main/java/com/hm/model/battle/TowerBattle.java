package com.hm.model.battle;


import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import com.hm.config.MissionConfig;
import com.hm.enums.BattleType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TowerBattle extends MissionBattle {

	private int maxHistory; // 历史最大关

	private int lastCnt = 1;// 剩余次数 默认一次
	private transient int buffCnt;// 已选择buff的次数
	// id - 等级
	private int [][] buffsLv = new int[4][2];
	// 随机的buff列表
	private List<Integer> randBuffIds = Lists.newArrayList();
	// 自动选择预设buff是否启用
	public int auto;
	// 预设buff类型
	private int [] preBuffType = new int[4];


	public TowerBattle(){
		super(BattleType.TowerBattle);
	}

	@Override
	public boolean isCanFight(Player player, int id) {
		if (getCurId() == maxHistory){// 打到最大关 判断下一关
			return super.isCanFight(player, id);
		}
		if (getCurId() < maxHistory){
			return true;
		}
		return false;
	}

	@Override
	public void resetDay(BasePlayer player) {
		clearCurId();
		MissionConfig missionConfig = SpringUtil.getBean(MissionConfig.class);
		this.randBuffIds = missionConfig.randomBuffIds();
		this.buffCnt = 0;
		this.lastCnt = maxHistory <= 0 ? 1 : 0;
		this.buffsLv = new int[4][2];
		this.auto = 0;
	}

	public void addBuff(int id, int index){
		if (id > 0){// id小于等于0  放弃
			int oldId = getBuffId(index);
			if (oldId == id){//替换掉原来的
				int lv = getBuffLv(index);
				setBuffLv(index,lv + 1);
			} else {
				setBuffId(index, id);
				setBuffLv(index, 1);
			}
		}
		addBuffCont();
	}

	public void addBuffCont() {
		this.buffCnt ++;
		this.randBuffIds.clear();
	}

	public int getBuffId(int index){
		return buffsLv[index][0];
	}

	public int getBuffLv(int index){
		return buffsLv[index][1];
	}

	public void setBuffId(int index, int id){
		this.buffsLv[index][0] = id;
	}

	public void setBuffLv(int index, int lv){
		this.buffsLv[index][1] = lv;
	}

	// 获取buff位置
	public int getBuffIndex(int id){
		if (id <= 0){
			return -1;
		}
		for (int i = 0; i < buffsLv.length; i++) {
			if (buffsLv[i][0] == id){
				return i;
			}
		}
		return -1;
	}

	// 获取首个为空的buff位置
	public int getFirstEmptyIndex(int maxPosition){
		for (int i = 0; i < buffsLv.length; i++) {
			if (i > maxPosition){
				break;
			}
			if (buffsLv[i][0] <= 0){
				return i;
			}
		}
		return -1;
	}

	// 是否是首次胜利
	public boolean isFirstWin(int id){
		return id > maxHistory;
	}

	@Override
	public boolean isCanSweep(int id) {
		return getCurId() <= maxHistory;
	}
}
