package com.hm.enums;


public enum PayType {

	//0：测试数据，1：正式；2：直冲
	TestData("测试充值",0),
	Normal("正常充值",1),
	Gm("GM充值",2),
	
	;
	
	 // 成员变量
    private String name;
    private int code;
    
    private PayType(){
    	
    }
    
 // 构造方法
    private PayType(String name, int code) {
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

	public void setCode(int code) {
		this.code = code;
	}
	
	

}
