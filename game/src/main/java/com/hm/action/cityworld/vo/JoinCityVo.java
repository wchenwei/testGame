package com.hm.action.cityworld.vo;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.cityworld.troop.NpcCityTroop;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.player.SimplePlayerVo;
import lombok.Data;

import java.util.List;

@Data
public class JoinCityVo extends SimplePlayerVo{
	private int atkType;//0-进攻 1-防御
	private String guildName;
    public int num = 1;
	
	public JoinCityVo(Player player,Guild guild) {
		this.load(player);
		if(guild != null) {
			this.guildName = guild.getGuildInfo().getGuildName();
			this.loadGuild(guild);
		}
	}

    public JoinCityVo(List<BaseCityFightTroop> npcList) {
        NpcCityTroop firstNpc = (NpcCityTroop) npcList.get(0);
        NpcTroopTemplate template = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(firstNpc.getNpcId());
        LanguageCnTemplateConfig languageConfig = SpringUtil.getBean(LanguageCnTemplateConfig.class);
        this.icon = template.getHead_icon()+"";
        this.frameIcon = template.getHead_frame();
        this.name = languageConfig.getValue(template.getName());
        this.num = npcList.size();
    }
	
}
