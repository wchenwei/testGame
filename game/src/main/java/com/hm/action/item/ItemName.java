package com.hm.action.item;

import com.hm.enums.ItemType;

public class ItemName {
	private int type;
	private int id;
	private String name;
	
	public ItemName(ItemType type, int id, String name) {
		this.type = type.getType();
		this.id = id;
		this.name = name;
	}

	public ItemName(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}


	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
