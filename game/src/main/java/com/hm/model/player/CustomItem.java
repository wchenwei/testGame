package com.hm.model.player;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomItem {
	private String name;
	private String tanks;
	
	public CustomItem(String name, String tanks) {
		this.name = name;
		this.tanks = tanks;
	}
	
	
}
