package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_51_stage")
public class Active51StageTemplate {
	private Integer id;
	private String icon_resource;
	private String base_item;
	private String item_1;
	private String item_2;
	private String item_3;
	private Integer mail_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon_resource() {
		return icon_resource;
	}

	public void setIcon_resource(String icon_resource) {
		this.icon_resource = icon_resource;
	}
	public String getBase_item() {
		return base_item;
	}

	public void setBase_item(String base_item) {
		this.base_item = base_item;
	}
	public String getItem_1() {
		return item_1;
	}

	public void setItem_1(String item_1) {
		this.item_1 = item_1;
	}
	public String getItem_2() {
		return item_2;
	}

	public void setItem_2(String item_2) {
		this.item_2 = item_2;
	}
	public String getItem_3() {
		return item_3;
	}

	public void setItem_3(String item_3) {
		this.item_3 = item_3;
	}
	public Integer getMail_id() {
		return mail_id;
	}

	public void setMail_id(Integer mail_id) {
		this.mail_id = mail_id;
	}
}
