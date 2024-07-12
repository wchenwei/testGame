package com.hm.config;

import com.google.common.collect.Lists;

import java.util.List;

public class GameConfig {
	public static String GameVersion = getGameVersion();
	public static String StartDate = "";
	public static List<ExcleError> errorExcelList = Lists.newArrayList();
	
	public static void addErrorConfig(ExcleError excleError) {
		errorExcelList.add(excleError);
	}
	
	
	public static String getGameVersion() {
        return "0.2.1.0";
	}
}
