package com.hm.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.model.mail.Mail;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.servercontainer.mail.MailItemContainer;
import com.hm.servercontainer.mail.MailServerContainer;
import com.hm.util.PubFunc;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

public class MailUtils extends CommonDbUtil{
	
	public static Mail getMail(int serverId,String id) {
		MongodDB mongodDB = getMongoDB(serverId);
		if(mongodDB != null) {
			return mongodDB.get(id, Mail.class);
		}
		return null;
	}
	
	public static Mail getMail(String id) {
		if(StrUtil.startWith(id,"gm_")) {
			return null;
		}
		int serverId = PubFunc.parseInt(id.split("_")[0]);
		if(serverId <= 0) {
			return null;
		}
		Mail mail = getMail(serverId,id); 
		return mail; 
	}
	
	public static Mail getMailByCache(int serverId,String id) {
		MailItemContainer mailItemContainer = MailServerContainer.of(serverId);
		if(mailItemContainer != null) {
			return mailItemContainer.getMail(id);
		}
		return null;
	}
	
	
	public static void saveMail(Mail mail) {
		insert(mail);
	}
	
	public static void saveMailList(int serverId,List<Mail> mailList) {
		try {
			int size = mailList.size();
			int count = size%100 > 0 ?size/100+1:size/100;
			for (int i = 0; i < count; i++) {
				int start = i*100;
				int end = (i+1)*100;
				List<Mail> mList = CollUtil.sub(mailList, start, end);
				getMongoDB(serverId).insertAll(mList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Mail> getMailList(int serverId,Collection<String> ids){
		Query query = Query.query(Criteria.where("_id").in(ids));
		return getList(query, Mail.class, serverId);
	}
	
	public static List<Mail> getMailList(int serverId,int type){
		Query query = Query.query(Criteria.where("sendType").is(type));
		return getList(query, Mail.class, serverId);
	}
}
