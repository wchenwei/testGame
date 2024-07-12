package com.hm.enums;


public enum MailSubType {
	ImageRecord(1,"镜像记录"),//镜像记录
	Link(2,"超链接"),//超链接
	;



	 // 成员变量
    private String name;
    private int code;

    private MailSubType(){

    }

 	// 构造方法
    private MailSubType(int code, String name) {
        this.name = name;
        this.code = code;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public String getStrCode() {
		return ""+code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	

}
