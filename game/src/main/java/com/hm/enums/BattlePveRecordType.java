package com.hm.enums;

import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.config.excel.templaextra.MissionSpecailExtraTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.item.Items;
import lombok.Getter;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 战役记录类型
 * @date 2023/10/18 14:57
 */
@Getter
public enum BattlePveRecordType {
	MainMission(0,null,"主线城池") {
		@Override
		public List<Items> getRecordItems(int id) {
			MissionExtraTemplate template = SpringUtil.getBean(MissionConfig.class).getMission(id);
			return template == null?null:template.getRecordRewardList();
		}

		@Override
		public List<Integer> buildSortIds() {
			return null;
		}
	}
	,
	TowerBattle(1,BattleType.TowerBattle,"使命之路"){
		@Override
		public List<Items> getRecordItems(int id) {
			MissionSpecailExtraTemplate template = SpringUtil.getBean(MissionConfig.class).getBattle(id);
			return template == null?null:template.getRecordRewardList();
		}
		@Override
		public List<Integer> buildSortIds() {
			return SpringUtil.getBean(MissionConfig.class)
					.getBattleHaveRecordReward(BattleType.TowerBattle.getType());
		}
	},
	;


	private BattlePveRecordType(int type, BattleType battleType,String desc) {
		this.type = type;
		this.desc = desc;
		this.battleType = battleType;
	}
	private int type;
	private String desc;
	private BattleType battleType;
	private List<Integer> sortIdList;//可以领取的所有关卡id列表

	public abstract List<Items> getRecordItems(int missionId);
	public abstract List<Integer> buildSortIds();

	public List<Integer> getSortIdList() {
		if(this.sortIdList == null) {
			this.sortIdList = buildSortIds();
		}
		return sortIdList;
	}


	public String buildRecordId(int missionId) {
		return this.type+"_"+missionId;
	}


	public static BattlePveRecordType get(int type) {
		for (BattlePveRecordType buildType : BattlePveRecordType.values()) {
			if (buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

	public static BattlePveRecordType getRecordType(int battleType,int id) {
		if(battleType == BattleType.TowerBattle.getType()) {
			return BattlePveRecordType.TowerBattle;
		}
		return null;
	}
}
