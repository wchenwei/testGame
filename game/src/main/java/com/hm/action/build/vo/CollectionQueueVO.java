package com.hm.action.build.vo;

import com.hm.enums.QueueType;
import com.hm.model.item.Items;

public class CollectionQueueVO {
	private QueueType queueType;
	private int tempId;
	private int type;
	private int lv;
	private Items item;
	public QueueType getQueueType() {
		return queueType;
	}
	public void setQueueType(QueueType queueType) {
		this.queueType = queueType;
	}
	public int getTempId() {
		return tempId;
	}
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public Items getItem() {
		return item;
	}
	public void setItem(Items item) {
		this.item = item;
	}
	

}
