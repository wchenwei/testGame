package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.ActivePrivateGiftTemplate;

import java.util.List;
import java.util.Map;

@FileConfig("active_private_gift")
public class ActivePrivateGiftImpl extends ActivePrivateGiftTemplate {
	//位置，跟位置能选的library
	private Map<Integer, List<Integer>> libMap = Maps.newHashMap();
	//有几个奖励格子
	private int size=0;
	
	public void init() {
		String[] strArray = this.getLibrary().split(";");
		for(int i=0; i<strArray.length; i++) {
			this.libMap.put(i+1, StringUtil.splitStr2IntegerList(strArray[i], ","));
		}
		size = strArray.length;
    }
	
	public List<Integer> getLibrary(int index) {
		return this.libMap.get(index);
	}

	public boolean fitLv(int playerLv) {
		return playerLv>=this.getLv_down() && playerLv<=this.getLv_up();
	}
	
	public boolean checkData(int index, int libreryId) {
		return libMap.keySet().contains(index) && libMap.get(index).contains(libreryId);
	}
	public int getSize() {
		return size;
	}
}
