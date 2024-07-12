package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("broadcast")
public class BroadcastTemplate {
	private Integer id;
	private Integer rank;
	private String key;
	private Integer display_type;
	private Integer chat_channel;
	private Integer maxtimes;
	private String parms;
	private Integer obId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public Integer getDisplay_type() {
		return display_type;
	}

	public void setDisplay_type(Integer display_type) {
		this.display_type = display_type;
	}
	public Integer getChat_channel() {
		return chat_channel;
	}

	public void setChat_channel(Integer chat_channel) {
		this.chat_channel = chat_channel;
	}
	public Integer getMaxtimes() {
		return maxtimes;
	}

	public void setMaxtimes(Integer maxtimes) {
		this.maxtimes = maxtimes;
	}
	public String getParms() {
		return parms;
	}

	public void setParms(String parms) {
		this.parms = parms;
	}
	public Integer getObId() {
		return obId;
	}

	public void setObId(Integer obId) {
		this.obId = obId;
	}
}
