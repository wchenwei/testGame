package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("push_message")
public class PushMessageTemplate {
	private Integer id;
	private String time;
	private Integer all_player;
	private String message_title;
	private String message_txt;
	private Integer last_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public Integer getAll_player() {
		return all_player;
	}

	public void setAll_player(Integer all_player) {
		this.all_player = all_player;
	}
	public String getMessage_title() {
		return message_title;
	}

	public void setMessage_title(String message_title) {
		this.message_title = message_title;
	}
	public String getMessage_txt() {
		return message_txt;
	}

	public void setMessage_txt(String message_txt) {
		this.message_txt = message_txt;
	}
	public Integer getLast_time() {
		return last_time;
	}

	public void setLast_time(Integer last_time) {
		this.last_time = last_time;
	}
}
