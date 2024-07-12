package com.hmkf.config.template;

import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@FileConfig("npc_arena")
public class NpcArenaExTemplate extends NpcArenaTemplate{
	private List<Integer> npcIdList;
	private NpcTroopTemplate[] npcTemplate;
	private long power;

	@ConfigInit
	public void init() {
		this.npcIdList = StringUtil.splitStr2IntegerList(getEnemy_config(), ",");

		this.npcTemplate = new NpcTroopTemplate[this.npcIdList.size()];
		for (int i = 0; i < npcIdList.size(); i++) {
			this.npcTemplate[i] = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(this.npcIdList.get(i));
		}
		this.power = Arrays.stream(npcTemplate).mapToLong(e -> e.getPower()).sum();
	}

	public NpcTroopTemplate getFirstNpcTemplate() {
		return npcTemplate[0];
	}
}
