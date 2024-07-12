package com.hm.model.guild.tactics.vo;

import com.hm.model.guild.tactics.FinanceTatics;
import com.hm.model.item.Items;

public class FinanceTaticsVo extends BaseGuildTacticsVo{
	private Items items;
	
	public FinanceTaticsVo(FinanceTatics tactics) {
		super(tactics);
		this.items = tactics.getItems();
	}
	
}
