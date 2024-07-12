package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("help")
public class HelpTemplate {
	private Integer id;
	private Integer type_main;
	private Integer type_sub;
	private String help_txt;
	private String tips_txt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType_main() {
		return type_main;
	}

	public void setType_main(Integer type_main) {
		this.type_main = type_main;
	}
	public Integer getType_sub() {
		return type_sub;
	}

	public void setType_sub(Integer type_sub) {
		this.type_sub = type_sub;
	}
	public String getHelp_txt() {
		return help_txt;
	}

	public void setHelp_txt(String help_txt) {
		this.help_txt = help_txt;
	}
	public String getTips_txt() {
		return tips_txt;
	}

	public void setTips_txt(String tips_txt) {
		this.tips_txt = tips_txt;
	}
}
