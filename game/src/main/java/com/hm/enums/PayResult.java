package com.hm.enums;


public enum PayResult {

	////0、未支付；1、支付成功；2、支付失败
	//不能修改，对应游戏服务器中的PayAction
	NORMAL("未支付",0),
	SUCCESS("支付成功",1),
	ERROR("支付失败，服务器异常",2),
	SRVERNOEXIT("服务器不存在", 3),
	SERVERERROR("服务器返回错误", 4),
	GameServerParam("游戏服传入参数校验错误", 5),
	GameServerError("游戏服充值异常", 6),
	GameServerSuccess("游戏服充值成功", 7),
	;
	
	
	
	 // 成员变量
    private String name;
    private int code;
    
    private PayResult(){
    	
    }
    
 // 构造方法
    private PayResult(String name, int code) {
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
