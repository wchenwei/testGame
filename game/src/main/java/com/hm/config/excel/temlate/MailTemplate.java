package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mail")
public class MailTemplate {
	private Integer mail_id;
	private String mail_title;
	private String mail_content;
	private String mail_send;
	private String mail_receive;
	private Integer mail_time;
	private String mail_drop;

	public Integer getMail_id() {
		return mail_id;
	}

	public void setMail_id(Integer mail_id) {
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
	public String getMail_send() {
		return mail_send;
	}

	public void setMail_send(String mail_send) {
		this.mail_send = mail_send;
	}
	public String getMail_receive() {
		return mail_receive;
	}

	public void setMail_receive(String mail_receive) {
		this.mail_receive = mail_receive;
	}
	public Integer getMail_time() {
		return mail_time;
	}

	public void setMail_time(Integer mail_time) {
		this.mail_time = mail_time;
	}
	public String getMail_drop() {
		return mail_drop;
	}

	public void setMail_drop(String mail_drop) {
		this.mail_drop = mail_drop;
	}
}
