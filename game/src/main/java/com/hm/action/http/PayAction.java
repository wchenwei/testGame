/**  
 * Project Name:SLG_GameHot.
 * File Name:BroadcasetAction.java  
 * Package Name:com.hm.action.http  
 * Date:2018年5月17日上午9:17:56  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.http;

import com.alibaba.fastjson.JSON;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.action.recharge.RechargeBiz;
import com.hm.enums.PayResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 
 * ClassName: PayAction. <br/>  
 * Function: 支付服务器发送过来的支付请求. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年6月5日 下午5:14:35 <br/>  
 *  
 * @author zxj  
 * @version
 */
@Slf4j
@Service("pay.do")
public class PayAction {
	

	@Resource
	private RechargeBiz rechargeBiz;
	
	public void pay(HttpSession session) {
		//需要playerid，rechargeid，price
		Map<String, String> params = session.getParams();
		log.info("======支付服务器======支付服务器发起支付请求：" + JSON.toJSONString(params));
		if(!this.checkParam(params)) {
			log.error("======支付服务器======支付服务器支付失败：" + JSON.toJSONString(params));
			session.Write(PayResult.GameServerParam.getStrCode());
			return;
		}
		
		try {
			boolean result = rechargeBiz.rewardPlayer(params);
			if(result) {
				session.Write(PayResult.GameServerSuccess.getStrCode());
				return;
			}
		} catch (Exception e) {
			log.error("======支付服务器======支付服务器支付失败：" + JSON.toJSONString(params), e);
		}
		session.Write(PayResult.GameServerError.getStrCode());
	}
	
	/*postParam.put("userid", param.get("userid"));
	postParam.put("rmb", param.get("rmb"));
	postParam.put("productid", param.get("productid"));*/
	//校验出入参数是否正确
	private boolean checkParam(Map<String, String> params) {
		if(!params.containsKey("userid") || StringUtils.isEmpty(params.get("userid"))) {
			return false;
		}
		if(!params.containsKey("rmb") || StringUtils.isEmpty(params.get("rmb"))) {
			return false;
		}
		if(!params.containsKey("productid") || StringUtils.isEmpty(params.get("productid"))) {
			return false;
		}
		return true;
	}
}












