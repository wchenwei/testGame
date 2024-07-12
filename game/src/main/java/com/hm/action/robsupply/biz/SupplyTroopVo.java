package com.hm.action.robsupply.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.SimplePlayerVo;
import com.hm.model.supplytroop.SupplyTroop;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;

import java.util.List;

public class SupplyTroopVo extends SimplePlayerVo{
	private String id;
	private long endTime;//结束时间
	private int cityId;
	private int star;
	private Items item;
	private boolean isDouble;//是否双倍
	private List<Integer> wayList = Lists.newLinkedList();
	private boolean isNpc;
	
	public SupplyTroopVo(SupplyTroop supplyTroop) {
		this.id = supplyTroop.getId();
		this.endTime = supplyTroop.getEndTime();
		this.cityId = supplyTroop.getSupplyItem().getCityId();
		this.star = supplyTroop.getSupplyItem().getStar();
		this.item = supplyTroop.getSupplyItem().getItem();
		this.wayList = supplyTroop.getSupplyItem().getWayList();
		this.isDouble = supplyTroop.getSupplyItem().isDouble();
		
		PlayerRedisData playerData = RedisUtil.getPlayerRedisData(supplyTroop.getPlayerId());
		if(playerData != null) {
			this.load(playerData);
			this.loadGuild(supplyTroop);
		}
	}
	
	public SupplyTroopVo(NpcRobSupply npcRobSupply) {
		this.id = npcRobSupply.getId();
		this.star = npcRobSupply.getSupplyItem().getStar();
		this.item = npcRobSupply.getSupplyItem().getItem();
		this.isNpc = true;
		NpcTroopTemplate template = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(npcRobSupply.getNpcId());
		LanguageCnTemplateConfig languageConfig = SpringUtil.getBean(LanguageCnTemplateConfig.class);
		this.name = languageConfig.getValue(template.getName());
		this.lv = template.getLevel();
		this.icon = template.getHead_icon()+"";
		this.frameIcon = template.getHead_frame();
	}
}
