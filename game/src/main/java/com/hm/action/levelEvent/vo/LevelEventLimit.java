package com.hm.action.levelEvent.vo;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.TankConfig;
import com.hm.enums.LevelEventLimitType;
import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelEventLimit {
	private int id;
	private int type;
	private int num;
	public int getId() {
		return id;
	}
	public int getType() {
		return type;
	}
	public int getNum() {
		return num;
	}
	
	public boolean checkResult(Player player,List<Integer> tankIds){
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		LevelEventLimitType limitType = LevelEventLimitType.getType(id);
		switch(limitType){
			case PlayerLevel:
				return player.playerLevel().getLv()>=num;
			case PlayerCombat:
				return player.getPlayerDynamicData().getCombat()>=num;
			case TankCamp://上阵坦克全部为type阵营的tank
				return !(tankIds.stream().anyMatch(t ->tankConfig.getTankSetting(t).getCountry()!=type));
			case TankCampNum://上阵坦克必须为type阵营的tank 大于=N辆
				return tankIds.stream().filter(t -> tankConfig.getTankSetting(t).getCountry()==type).count()>=num;
			case TankCampLimit://上阵坦克不能有阵营为type的坦克
				return !(tankIds.stream().anyMatch(t -> tankConfig.getTankSetting(t).getCountry()==type));
			case TankType://上阵坦克全部为type类型的tank
				return !(tankIds.stream().anyMatch(t ->tankConfig.getTankSetting(t).getType()!=type));
			case TankTypeNum://上阵坦克必须为type阵营的tank 大于=N辆
				return tankIds.stream().filter(t -> tankConfig.getTankSetting(t).getType()==type).count()>=num;
			case TankTypeLimit://上阵坦克不能有阵营为type的坦克
				return !(tankIds.stream().anyMatch(t -> tankConfig.getTankSetting(t).getType()==type));
			case TankStar://上阵坦克必须全部达到X星
				return !(tankIds.stream().anyMatch(t ->player.playerTank().getTank(t).getStar()<type));
			case TankStarNum://上阵坦克必须有N辆达到X星的
				return !(tankIds.stream().filter(t ->player.playerTank().getTank(t).getStar()>=type).count()>=num);
			case TankReLv://上阵坦克必须全部达到X阶
				return !(tankIds.stream().anyMatch(t ->player.playerTank().getTank(t).getReLv()<type));
			case TankReLvNum://上阵坦克必须有N辆达到X阶的
				return !(tankIds.stream().filter(t ->player.playerTank().getTank(t).getReLv()>=type).count()>=num);
			case TankGrade://上阵坦克必须全部为X资质以上的
				return !(tankIds.stream().anyMatch(t ->tankConfig.getTankSetting(t).getRare()<type));
			case TankGradeNum://上阵坦克必须全部为X资质以上的N辆
				return tankIds.stream().filter(t ->tankConfig.getTankSetting(t).getRare()>=type).count()>=num;
			case TankNum://上阵坦克N辆
				return tankIds.size()<=num;
		}
		return false;
	}

}
