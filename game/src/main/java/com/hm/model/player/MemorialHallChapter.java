package com.hm.model.player;

import java.util.Arrays;

/**
 * @Description: 照片章节
 * @author siyunlong  
 * @date 2019年12月23日 上午10:17:58 
 * @version V1.0
 */
public class MemorialHallChapter {
	//照片墙,每个墙的照片数量
	private int[] photos = new int[20];
	private int lv;//等级
	
	public int getTotalPohoto() {
		return Arrays.stream(photos).sum();
	}
	
	public int getPohotoNum(int index) {
		return photos[index-1];
	}
	
	public void addWall(int index,long addNum) {
		this.photos[index-1] += addNum;
	}

	public int[] getPhotos() {
		return photos;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}
	
}
