package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("city_island")
public class CityIsLandTemplate {
	private int id;
	private int level;
	private double add;

	private int map_type;
}
