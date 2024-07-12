package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("kf_scores_city")
public class KfScoresCityTemplate {
	private int id;
	private int city_type;
	private int is_main_city;
	private int reset_city;//野蛮人城池
}
