package com.hm.war.sg;

import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.templaextra.MilitaryExtraTemplate;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerCommander;
import com.hm.war.sg.buff.SkillPreBuff;
import com.hm.war.sg.leader.BigLeaderSkill;
import com.hm.war.sg.leader.NormalLeaderSkill;
import lombok.Getter;
import org.springframework.data.annotation.Transient;

/**
 * 
 * @Description: 战斗坐骑
 * @author siyunlong  
 * @date 2018年11月8日 上午10:41:12 
 * @version V1.0
 */
@Getter
public class GroupHorse {
	//==============座驾====================
	private int lv;
	private int militaryLv;//军衔等级

	//主角普通攻击技能
	@Transient
	private transient NormalLeaderSkill normalSkill;
	//主角大招技能
	@Transient
	private transient BigLeaderSkill bigSkill;


	//检查是否可以释放技能
	public void checkCanReleaseSkill(Frame frame, UnitGroup atkGroup, UnitGroup defGroup) {
		if(this.normalSkill == null || !this.normalSkill.checkCD(frame)) {
			return;
		}
		//检查是否可以释放大招技能
		if(this.bigSkill != null && atkGroup.isCanReleaseSkill() && this.bigSkill.checkReleaseSkill(frame,atkGroup,defGroup)) {
			//推辞检查
			SkillPreBuff skillPreBuff = this.bigSkill.getSkillPreBuff();
			if(skillPreBuff != null) {
//				this.normalSkill.changeNextFrame(skillPreBuff.getEndFrame());
			}else{
				this.normalSkill.changeNextFrame(frame.getId()+this.normalSkill.getCd());
			}
			return;//释放大招技能
		}
		//释放普通攻击技能
		this.normalSkill.checkReleaseSkill(frame,atkGroup,defGroup);
	}

	public void initHorse(Player player){
		PlayerCommander playerCommander = player.playerCommander();
		initHorse(playerCommander.getCarLv(), playerCommander.getMilitaryLv());
	}

	private void initHorse(int calLv, int militaryLv) {
		this.lv = calLv;
		this.militaryLv = militaryLv;
		//根据军衔等级
		if(this.militaryLv > 0) {
			MilitaryExtraTemplate template = SpringUtil.getBean(CommanderConfig.class).getMilitaryExtraTemplate(this.militaryLv);
			if(template.getNaskill_lv() > 0) {
				this.normalSkill = new NormalLeaderSkill(template.getNaskill_lv(),template.getNaskill_cd());
			}
			if(template.getKaskill_lv() > 0) {
				this.bigSkill = new BigLeaderSkill(template.getKaskill_lv(),template.getKaskill_cd());
			}
		}
	}

	public static void loadHorse(Player player,UnitGroup unitGroup) {
		GroupHorse groupHorse = new GroupHorse();
		groupHorse.initHorse(player);
		unitGroup.setGroupHorse(groupHorse);
	}

	public static void loadHorse(NpcTroopTemplate template, UnitGroup unitGroup) {
		GroupHorse groupHorse = new GroupHorse();
		groupHorse.initHorse(template.getCar_lv(), template.getMilitary_lv());
		unitGroup.setGroupHorse(groupHorse);
	}
}
