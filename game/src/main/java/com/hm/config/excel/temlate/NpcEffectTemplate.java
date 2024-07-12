package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("npc_effect")
public class NpcEffectTemplate {
	private Integer id;
	private String bonesPath;
	private String bonesName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getBonesPath() {
		return bonesPath;
	}

	public void setBonesPath(String bonesPath) {
		this.bonesPath = bonesPath;
	}
	public String getBonesName() {
		return bonesName;
	}

	public void setBonesName(String bonesName) {
		this.bonesName = bonesName;
	}
}
