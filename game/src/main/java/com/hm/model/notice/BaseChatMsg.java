package com.hm.model.notice;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.enums.SysMsgType;
import org.springframework.data.annotation.Transient;

import java.util.Date;


/**
 * 邮件基类，所有邮件继承此类
 * ClassName: Mail. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2017年12月18日 下午3:38:42 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class BaseChatMsg extends DBEntity<String>{
	private int msgType; 
	private String content;
	private transient Date date;
	@Transient 
	private long time; 
	
	public BaseChatMsg(){
		
	}
	public BaseChatMsg(SysMsgType msgType){
		this.msgType = msgType.getType();
		this.date = new Date();
		this.time = date.getTime()/1000; 
		
	}
	public int getMsgType() {
		return msgType;
	}
	
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public long getTime() {
		return time;
	}
	
}
