package com.hm.cache;

import com.google.common.cache.*;
import com.hm.db.MailUtils;
import com.hm.model.mail.Mail;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 
 * ClassName: WarRecordCacheManager. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年2月7日 上午10:40:26 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
@Slf4j
public class MailCacheManager {
	private static final MailCacheManager instance = new MailCacheManager();
	public static MailCacheManager getInstance() {
		return instance;
	}

	protected static final String CACHESPEC = "expireAfterWrite=5m";
	private final LoadingCache<String, Mail> cache;
	private final Mail DefaultValue = new Mail();
	
	private MailCacheManager() {
		cache = CacheBuilder.from(CACHESPEC)
			.maximumSize(10000)
			.removalListener(new RemovalListener<String, Mail>() {
				@Override
				public void onRemoval(RemovalNotification<String, Mail> notification) {
					  log.debug("remove cache MailId:"+notification.getKey());
				}
			})
			.build(new CacheLoader<String, Mail>() {
			@Override
			public Mail load(String key) {
				Mail mail = MailUtils.getMail(key);
				return mail != null?mail:DefaultValue;
			}
		});
	}
	public Mail getMail(String id) {
		Mail temp = cache.getUnchecked(id);
		if(temp != DefaultValue) {
			return temp;
		}
		return null;
	}
	
	public void addMail(Mail mail) {
		cache.put(mail.getId(), mail);
	}
	public void removeMail(String id) {
		cache.invalidate(id);
	}
}
