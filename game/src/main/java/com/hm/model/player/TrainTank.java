package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.model.item.Items;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainTank {
	private int id;
	private int type;
	private List<Items> rewards = Lists.newArrayList();
	

}
