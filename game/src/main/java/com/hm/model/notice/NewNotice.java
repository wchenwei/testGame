
package com.hm.model.notice;

import com.hm.enums.NoticeTypeEnum;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 
 * ClassName: 广播消息bean. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月2日 下午5:59:29 <br/>  
 * 停服、关服、特殊事件等
 * @author zxj  
 * @version
 */
@Getter
@Setter
@RedisMapperType(type = MapperType.STRING_HASH)
public class NewNotice extends BaseEntityMapper<String> {
	//============数据库存储===================
	private long startTime;//开始时间
	private long endTime;//结束时间
	private long nextTime;//下次发送时间
	private int type;	//对应的NoticeTypeEnum
	private int times;//发送次数
	private String contentBase;//初始化的发送内容
	private int interval;//发送时间间隔
	
	//=============发送客户端数据=======================
	private String content;//发送内容
	private int rank;//排序
	
	private int maxTimes;
	private int broadcastid;
	
	public int getMaxTimes() {
		return maxTimes;
	}
	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}
	public NewNotice() {}
	
	public NewNotice(long startTime, long endTime, NoticeTypeEnum sys, String content) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = sys.getType();
		this.setContent(content);
	}
	//返回用户大喇叭的信息
	public NewNotice(NoticeTypeEnum sys, String content) {
		this.type = sys.getType();
		this.setContent(content);
	}
	//此方法根据发送次数发送，审核过之后立马发送。
	public NewNotice(Map<String, String> params) {
		int serverId = Integer.parseInt(params.get("serverId"));
		int maxTimes = null==params.get("maxTimes")?10:Integer.parseInt(params.get("maxTimes"));
		this.setMaxTimes(maxTimes);
		this.setTimes(0);
		this.setId(String.format("%s_%s_%s", serverId,PrimaryKeyWeb.getPrimaryKey("notice",serverId),"s"));
		this.setServerId(serverId);
		this.setNextTime(new Date().getTime());
		this.setContentBase(params.get("content"));
		this.setInterval(Integer.parseInt(params.get("interval")));
		this.setBroadcastid(params.containsKey("broadcastid")?Integer.parseInt(params.get("broadcastid")):0);
	}
	public void setType(NoticeTypeEnum noticeType) {
		this.type = noticeType.getType();
	}
}








