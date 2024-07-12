package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.model.tank.Tank;
import com.hm.war.sg.GroupHorse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expedition extends SimplePlayerVo{
	private int superWeaponLv;//超武等级
	private List<Tank> tanks = Lists.newArrayList();//守将tank
	//坐骑附加技能
	private Set<Integer> commanderSkillList;
	//坐骑属性
	private GroupHorse groupHorse;
	private List<Tank> soldiers = Lists.newArrayList();
	
	public Expedition(Player player) {
		this.load(player);
		this.superWeaponLv = player.playerCommander().getSuperWeaponLv();
	}
	
	public void updateTank(int id,long hp,long mp) {
		Tank tank = this.tanks.stream().filter(t ->t.getId()==id).findFirst().orElse(null);
		if(tank==null){
			return;
		}
		tank.setCurHp(hp);
		tank.setMp(mp);
	}
	public Tank getTank(int tankId) {
		return tanks.stream().filter(t -> t.getId() == tankId).findAny().orElse(null);
	}

}
