package com.hm.war.sg.event;

import com.hm.model.item.Items;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class DropItemEvent extends Event{
	private List<Items> dropItems;

	public DropItemEvent(int id,List<Items> dropItems) {
		super(id, EventType.DropItemEvent);
		this.dropItems = dropItems;
	}
}
