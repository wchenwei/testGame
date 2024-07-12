/**
 * 
 */
package com.hm.libcore.rmi;

import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Title: RmiServerHandle.java 
 * Description: Copyright: Copyright (c) 2014 
 * Company:Hammer Studio
 * @author 叶亮
 * @date 2014年10月28日 下午5:36:42
 * @version 1.0
 */
@Slf4j
public class RmiServerHandle implements IRmiHandle {

	@Override
	public RmiResponse onRmiRequest(RmiRequest rmiRequest) {
		RmiResponse rmiResponse = new RmiResponse();
		try{
			Object obj = SpringUtil.getBean(rmiRequest.getAction());
			if (obj == null) {
				rmiResponse.setCode((byte) 0);
				rmiResponse.setResult(RmiError.RMI_ERROR.getCode());
			}
			Method[] methods = obj.getClass().getMethods();
			boolean isHaveMethod = false;
			for(Method m : methods){//循环该类的每个方法
				if(m.getName().equals(rmiRequest.getMethod())){
					isHaveMethod = true;
					break;
				}
			
			}
			if(isHaveMethod){
				Object result = obj.getClass().getMethod(rmiRequest.getMethod(), RmiRequest.class)
						.invoke(obj, rmiRequest);
				if(result != null){
					rmiResponse.setResult(JSONUtil.toJson(result));
				}
				rmiResponse.setCode((byte)1);
			}else{
				rmiResponse.setCode((byte) 0);
				rmiResponse.setResult(RmiError.RMI_ERROR.getCode());
			}
		}catch(Exception e){
			log.error("RMI远程调用出错", e);
		}
		return rmiResponse;
	}

}
