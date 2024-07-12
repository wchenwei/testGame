package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("sevenday_mail")
public class SevendayMailTemplate {
	private Integer id;
	private String mail_id;
	private String mail_title;
	private String mail_content;
	private String mail_drop;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getMail_id() {
		return mail_id;
	}

	public void setMail_id(String mail_id) {
		this.mail_id = mail_id;
	}
	public String getMail_title() {
		return mail_title;
	}

	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}
	public String getMail_content() {
		return mail_content;
	}

	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}
	public String getMail_drop() {
		return mail_drop;
	}

	public void setMail_drop(String mail_drop) {
		this.mail_drop = mail_drop;
	}
}
