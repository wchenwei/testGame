/**  
 * Project Name:SLG_GameHot.
 * File Name:MailConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年4月10日下午3:03:16  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.enums.MailConfigEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * ClassName: MailConfig. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月10日 下午3:03:16 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Config
public class MailConfig extends ExcleConfig {
	
	private Map<Integer, MailTemplate> mailConfigMap = Maps.newHashMap();
	

	/**  
	 * TODO 简单描述该方法的实现功能（可选）.  
	 * @see ExcleConfig#loadConfig()
	 */
	@Override
	public void loadConfig() {
		loadMailTemplate();
	}

	/**  
	 * TODO 简单描述该方法的实现功能（可选）.  
	 * @see ExcleConfig#getDownloadFile()
	 */
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(MailTemplate.class);
	}
	
	private void loadMailTemplate(){
		List<MailTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(MailTemplate.class), new TypeReference<ArrayList<MailTemplate>>(){}));
		Map<Integer, MailTemplate> mailConfigMap = Maps.newConcurrentMap();
		for(MailTemplate temp:list){
			mailConfigMap.put(temp.getMail_id(), temp);
		}
		this.mailConfigMap = ImmutableMap.copyOf(mailConfigMap);
	}
	public MailTemplate getMailTemplate(MailConfigEnum mailType) {
		return mailConfigMap.get(mailType.getType());
	}
	public MailTemplate getMailTemplate(int mailType) {
		return mailConfigMap.get(mailType);
	}
	
}






