package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mapoffY")
public class MapoffYTemplate {
	private Integer id;
	private String name;
	private Integer mapoffY;
	private String mapKey;
	private Integer mapKeyNums;
	private String filp;
	private String Smoke;

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
	public Integer getMapoffY() {
		return mapoffY;
	}

	public void setMapoffY(Integer mapoffY) {
		this.mapoffY = mapoffY;
	}
	public String getMapKey() {
		return mapKey;
	}

	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	public Integer getMapKeyNums() {
		return mapKeyNums;
	}

	public void setMapKeyNums(Integer mapKeyNums) {
		this.mapKeyNums = mapKeyNums;
	}
	public String getFilp() {
		return filp;
	}

	public void setFilp(String filp) {
		this.filp = filp;
	}
	public String getSmoke() {
		return Smoke;
	}

	public void setSmoke(String Smoke) {
		this.Smoke = Smoke;
	}
}
