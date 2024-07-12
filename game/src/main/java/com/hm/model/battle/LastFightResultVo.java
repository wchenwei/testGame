package com.hm.model.battle;

import com.hm.action.mission.vo.MissionResultVo;
import com.hm.model.item.Items;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastFightResultVo {
	private long fightId;
	private MissionResultVo result;

	public LastFightResultVo(long fightId, List<Items> rewards) {
		this.fightId = fightId;
		MissionResultVo vo = new MissionResultVo();
        vo.setRewards(rewards);
        this.result = vo;
	}
}
