package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("museum_chapter")
public class MuseumChapterTemplate {
	private Integer id;
	private String chapter_name;
	private String chapter_desc;
	private String task_desc;
	private String photo_num;
	private String mark_value;
	private String attri;
	private Integer unlock;
	private Integer shop_unlock;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getChapter_name() {
		return chapter_name;
	}

	public void setChapter_name(String chapter_name) {
		this.chapter_name = chapter_name;
	}
	public String getChapter_desc() {
		return chapter_desc;
	}

	public void setChapter_desc(String chapter_desc) {
		this.chapter_desc = chapter_desc;
	}
	public String getTask_desc() {
		return task_desc;
	}

	public void setTask_desc(String task_desc) {
		this.task_desc = task_desc;
	}
	public String getPhoto_num() {
		return photo_num;
	}

	public void setPhoto_num(String photo_num) {
		this.photo_num = photo_num;
	}
	public String getMark_value() {
		return mark_value;
	}

	public void setMark_value(String mark_value) {
		this.mark_value = mark_value;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public Integer getUnlock() {
		return unlock;
	}

	public void setUnlock(Integer unlock) {
		this.unlock = unlock;
	}
	public Integer getShop_unlock() {
		return shop_unlock;
	}

	public void setShop_unlock(Integer shop_unlock) {
		this.shop_unlock = shop_unlock;
	}
}
