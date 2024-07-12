package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ArenaRefreshTemplate;

@FileConfig("arena_refresh")
public class ArenaRefreshExTemplate extends ArenaRefreshTemplate{
	private int[][] pos = new int[4][2];
	
	public  void init(){
		pos[0][0]= Integer.valueOf(getPos_1().split(",")[0]); 
		pos[0][1]= Integer.valueOf(getPos_1().split(",")[1]); 
		pos[1][0]= Integer.valueOf(getPos_2().split(",")[0]); 
		pos[1][1]= Integer.valueOf(getPos_2().split(",")[1]); 
		pos[2][0]= Integer.valueOf(getPos_3().split(",")[0]); 
		pos[2][1]= Integer.valueOf(getPos_3().split(",")[1]); 
		pos[3][0]= Integer.valueOf(getPos_4().split(",")[0]); 
		pos[3][1]= Integer.valueOf(getPos_4().split(",")[1]); 
		
	}

	public int[][] getPos() {
		return pos;
	}

	
	
}
