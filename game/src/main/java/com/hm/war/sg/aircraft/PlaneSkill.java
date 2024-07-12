package com.hm.war.sg.aircraft;

import com.hm.config.excel.templaextra.ItemBattleplaneTemplateImpl;
import com.hm.model.player.Aircraft;
import com.hm.war.sg.skillnew.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@NoArgsConstructor
@Data
public class PlaneSkill {
	private int id;
	private transient int skillId;
	private int mp;//所需
	private int star;
	
	@Transient
	private transient Skill skill;

	public PlaneSkill(Aircraft aircraft,ItemBattleplaneTemplateImpl template) {
		this.id = template.getId();
		this.skillId = template.getSkill_id();
		this.mp = template.getSkill_cost();
		this.star = aircraft.getStar();
	}
	
	
}
