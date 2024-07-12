package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveTaskTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;
import com.hm.util.ItemUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@FileConfig("active_task")
public class ActivityTaskTemplate extends ActiveTaskTemplate implements ITaskConfTemplate {
	private List<Items> rewards = Lists.newArrayList();
	private TaskTypeEnum taskType;
	private List<Items> resetCosts = Lists.newArrayList();
	private List<CombatDto> list = Lists.newArrayList();

	public void init() {
		rewards = ItemUtils.str2DefaultItemImmutableList(getTask_reward());
        taskType = TaskTypeEnum.num2enum(getTask_type());
		resetCosts = ItemUtils.str2DefaultItemImmutableList(getReset_cost());
    }

	public List<Items> getRewards() {
		return rewards;
	}

	public List<Items> getResetCosts(){
		return resetCosts;
	}

	public boolean isCanReset(){
		return this.getCan_reset()==1;
	}
	

	public long getNeedCombat(long combat){
		CombatDto combatDto = list.stream().filter(e -> e.isFit(combat)).findFirst().orElse(null);
		return combatDto == null ? 100000 : combatDto.getNeedCombat();
	}

	@Override
	public int getTaskFinish() {
		return getFinish_num();
	}


	@Override
	public TaskTypeEnum getTaskTypeEnum() {
		return taskType;
	}

	@Override
	public String getTaskFinishPara() {
		return getTask_finish();
	}

	@Override
	public boolean isFitObserver(ObservableEnum event) {
		TaskTypeEnum e = getTaskTypeEnum();
		return  e != null && e.isFitObserver(event);
	}

	public boolean isTaskUnlock(BasePlayer player) {
		return player.playerCommander().getMilitaryLv() >= this.getLevel_limit();
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class CombatDto{
		private long minCombat;
		private long maxCombat;
		// 需要提升的战力值
		private long needCombat;

		public boolean isFit(long combat){
			return combat >= minCombat && combat <= maxCombat;
		}
	}

}
