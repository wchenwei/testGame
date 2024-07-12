package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 玩家纪念馆
 * @author siyunlong  
 * @date 2019年12月23日 上午9:58:33 
 * @version V1.0
 */
public class PlayerMemorialHall extends PlayerDataContext{
	/**
	 * 每个照片的数量
	 */
	private ConcurrentHashMap<Integer, Long> photoMap = new ConcurrentHashMap<>();
	/**
	 * 每个章节的数据
	 */
	private Map<Integer,MemorialHallChapter> hallChapterMap = Maps.newConcurrentMap();
	/**
	 * 纪念值
	 */
	private long markVal;
	/**
	 * 纪念馆等级
	 */
	private int markLv;
	
	public void unlockChapters(int chapterId) {
		if(!this.hallChapterMap.containsKey(chapterId)) {
			this.hallChapterMap.put(chapterId, new MemorialHallChapter());
			SetChanged();
		}
	}
	public MemorialHallChapter getMemorialHallChapter(int chapterId) {
		return this.hallChapterMap.get(chapterId);
	}
	
	public void addPhoto(int id,long count) {
		this.photoMap.put(id, getPhotoNum(id)+count);
		SetChanged();
	}
	
	public long getPhotoNum(int tankId) {
		return this.photoMap.getOrDefault(tankId, 0L);
	}
	
	public boolean checkSpend(int tankId,long num) {
		return getPhotoNum(tankId) >= num;
	}
	
	public boolean spendPhotos(int tankId,long num) {
		long lastNum = getPhotoNum(tankId);
		if(lastNum >= num) {
			if(lastNum > num) {
				this.photoMap.put(tankId, lastNum-num);
			}else{
				this.photoMap.remove(tankId);
			}
			SetChanged();
			return true;
		}
		return false;
	}
	
	public Map<Integer, MemorialHallChapter> getHallChapterMap() {
		return hallChapterMap;
	}
	
	public void addMarkVal(int add) {
		this.markVal += add;
		SetChanged();
	}
	public long getMarkVal() {
		return markVal;
	}
	
	public int getMarkLv() {
		return this.markLv;
	}
	public void setMarkLv(int markLv) {
		this.markLv = markLv;
		SetChanged();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerMemorialHall", this);
	}
}