package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("dialogue")
public class DialogueTemplate {
	private Integer id;
	private String npc;
	private String npc_pos;
	private String talk_content;
	private Integer talk_skip;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}
	public String getNpc_pos() {
		return npc_pos;
	}

	public void setNpc_pos(String npc_pos) {
		this.npc_pos = npc_pos;
	}
	public String getTalk_content() {
		return talk_content;
	}

	public void setTalk_content(String talk_content) {
		this.talk_content = talk_content;
	}
	public Integer getTalk_skip() {
		return talk_skip;
	}

	public void setTalk_skip(Integer talk_skip) {
		this.talk_skip = talk_skip;
	}
}
