package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.db.MailUtils;
import com.hm.enums.MailState;
import com.hm.model.mail.Mail;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * 
 * ClassName: PlayerMail. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2017年12月18日 下午4:28:10 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class PlayerMail extends PlayerDataContext{
	private ConcurrentHashMap<String, Integer> sysMailMap = new ConcurrentHashMap<String, Integer>();  
	
	public int getMailState(String id){
		return sysMailMap.getOrDefault(id,MailState.Del.getType());
	}
	
	public List<String> getMailIdList() {
		List<String> idList = sysMailMap.entrySet().stream()
				.filter(e -> e.getValue() != MailState.Del.getType())
				.map(e -> e.getKey())
				.sorted((v1, v2) -> {
					if(v1.length() == v2.length()) {
						return v2.compareTo(v1);
					}
					return v2.length() - v1.length();
				})
				.collect(Collectors.toList());
		return idList;
	}
	/**
	 * 新邮件
	 * @author yanpeng
	 * @param id
	 */
	public void addMail(Mail mail) {
		if(mail.isHaveReward()){
			sysMailMap.put(mail.getId(), MailState.NewReward.getType());
		}else{
			sysMailMap.put(mail.getId(), MailState.NewMail.getType());
		}
		
		SetChanged();
	}
	/**
	 * 阅读邮件
	 * @author yanpeng
	 * @param id
	 */
	public void readMail(Mail mail) {
		if(mail.isHaveReward()){
			sysMailMap.put(mail.getId(),MailState.ReadNoGet.getType());
		}else{
			sysMailMap.put(mail.getId(),MailState.Read.getType());
		}
		
		SetChanged();
	}
	/**
	 * 领取附件
	 * @author yanpeng
	 * @param type
	 * @param id
	 */
	public void getReward(String id) {
		sysMailMap.put(id, MailState.Get.getType());
		SetChanged();
	}
	/**
	 * 能否领奖
	 * @author yanpeng
	 * @param mail
	 * @return
	 */
	public boolean canGetReward(Mail mail) {
		if(!mail.isHaveReward())
			return false;
		int state = sysMailMap.get(mail.getId());
		if(state == MailState.NewReward.getType() || state == MailState.ReadNoGet.getType()) {
			return true;
		}
		return false;
	}
	public boolean isHaveMail(Mail mail) {
		return sysMailMap.containsKey(mail.getId());
	}
	
	/**
	 * 全部已读并返回带附件的邮件id
	 *
	 * @author yanpeng 
	 * @return  
	 *
	 */
	public List<String> readAll(){
		List<String> list = Lists.newArrayList(); 
		sysMailMap.forEach((x,y)->{
			if(readAll(x,y)){
				list.add(x);
			}
		});
		SetChanged();
		return list; 
	}
	
	private boolean readAll(String id,int state){
		switch(MailState.getState(state)) {
			case NewMail:
				sysMailMap.put(id, MailState.Read.getType());
				return false; 
			case NewReward:
			case ReadNoGet:
				sysMailMap.put(id, MailState.Get.getType());
				return true; 
			default :
				return false; 
		}
	}
	
	
	/**
	 * 一键删除邮件
	 *
	 * @author yanpeng   
	 *
	 */
	public List<String> delAll() {
		int serverId = Context().getServerId();
		List<String> list = Lists.newArrayList(); 
		sysMailMap.forEach((id, state) -> {
			Mail mail = MailUtils.getMailByCache(serverId,id);
			if(mail != null) {
				if(MailState.isCanDel(state)) {
					list.add(id);
					if(mail.isCanDel()) {
						sysMailMap.remove(id);
					}else {
						sysMailMap.put(id, MailState.Del.getType());
					}
				}
			}
		});
		SetChanged();
		return list;
	}

	public boolean removeMail(String id){
		int serverId = Context().getServerId();
		Mail mail = MailUtils.getMailByCache(serverId,id);
		if(mail == null) {
			return true;
		}
		int state = getMailState(id);
		if(MailState.isCanDel(state)) {
			if(mail.isCanDel()) {
				sysMailMap.remove(id);
			}else {
				sysMailMap.put(id, MailState.Del.getType());
			}
			SetChanged();
			return true;
		}
		return false;
	}
	
	/**
	 * 删除过期邮件
	 *
	 * @author yanpeng   
	 *
	 */
	public void delExpiredMail(){
		int serverId = Context().getServerId();
		sysMailMap.forEach((id, state) -> {
			if(MailState.isCanDel(state)) {
				Mail mail = MailUtils.getMailByCache(serverId,id);
				if(mail == null || mail.isCanDel() && mail.isTimeExpired()) {
					System.err.println("邮件过期删除:"+id);
					sysMailMap.remove(id);
					SetChanged();
				}
			}
		});
	}
	
	/**
	 * 获取玩家邮件数量
	 *
	 * @author yanpeng 
	 * @return  
	 *
	 */
	public int getCount(){
		int size = 0; 
		for(Integer state:sysMailMap.values()){
			if(state != MailState.Del.getType()){
				size ++; 
			}
		}
		return size; 
	}
	
	public int getState(String id){
		return sysMailMap.get(id);
	}
	
	public ConcurrentNavigableMap<String, Integer> getMailSortMap() {
		ConcurrentSkipListMap<String, Integer> sortMap = new ConcurrentSkipListMap<String, Integer>((v1, v2) -> {
			if(v1.length() == v2.length()) {
				return v1.compareTo(v2);
			}
			return v1.length() - v2.length();
		});
		sortMap.putAll(this.sysMailMap);
		return sortMap.descendingMap();
	}
}
