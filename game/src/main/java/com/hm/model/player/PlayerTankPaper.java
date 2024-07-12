package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;

import java.util.List;
//战车图纸
public class PlayerTankPaper extends PlayerBagBase{
	
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("playerTankPaper", this);
	}
	
	public List<Items> getPaperList(List<Integer> listPapers) {
		List<Items> result = Lists.newArrayList();
		listPapers.forEach(e->{
			long paperCount = this.getItemCount(e);
			if(paperCount>0) {
				result.add(new Items(e,paperCount,ItemType.PAPER.getType()));
			}
		});
		return result;
	}
}
