package com.hm.model.activity.introductionWeb;

import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;

/**
 * 攻略活动
 * @author siyunlong  
 * @date 2020年5月14日 下午7:43:34 
 * @version V1.0
 */
public class IntroductionWebActivity extends AbstractActivity{
	private String url;
	
	public IntroductionWebActivity() {
		super(ActivityType.IntroductionWeb);
	}

	@Override
	public void loadExtend(String extend) {
		this.url = extend;
	}
}
