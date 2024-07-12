	package com.hm.enums;





public enum MailGroup {
	All(0,"全部"),
	Record(1,"战报"),
	Mail(2,"邮件"),
	Arena(3,"竞技场"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private MailGroup(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private String desc;
	

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	
	
	
}
