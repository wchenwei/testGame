package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("small_game_type")
public class SmallGameTypeTemplate {
	private Integer id;
	private String name;
	private Integer unlock_level;
	private Integer help_type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getUnlock_level() {
		return unlock_level;
	}

	public void setUnlock_level(Integer unlock_level) {
		this.unlock_level = unlock_level;
	}
	public Integer getHelp_type() {
		return help_type;
	}

	public void setHelp_type(Integer help_type) {
		this.help_type = help_type;
	}
}
