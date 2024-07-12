package com.hm.model.player;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.TitleConfig;
import com.hm.config.excel.temlate.PlayerTitleTemplate;
import lombok.Data;
import lombok.NoArgsConstructor;
/** 
 * @author 作者 xjt: 
 * @version 创建时间：2017年5月12日 上午9:31:25 
 * 称号
 */
@Data
@NoArgsConstructor
public class Title {
	private int titleId;
	private long endTime;//结束时间 -1为永久称号

	public Title(int titleId) {
		TitleConfig titleConfig = SpringUtil.getBean(TitleConfig.class);
		PlayerTitleTemplate template = titleConfig.getTitleTemplate(titleId);
		this.titleId = titleId;
		this.endTime = template.getLast_time()>0?System.currentTimeMillis() + GameConstants.DAY*template.getLast_time():-1;
	}
	
	public Title(int titleId, long endTime) {
		super();
		this.titleId = titleId;
		this.endTime = endTime;
	}
	
	/**
	 * 是否有效
	 * @return
	 */
	public boolean isValid(){
		return endTime==-1||endTime>System.currentTimeMillis();
	}
}
