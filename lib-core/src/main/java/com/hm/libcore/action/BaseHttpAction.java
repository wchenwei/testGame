package com.hm.libcore.action;


import com.hm.libcore.json.JSONResponse;
import com.hm.libcore.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class BaseHttpAction {

	
	protected String createJSONResponse(int code){
		JSONResponse resp = new JSONResponse();
		resp.setStatus(false);
		resp.setCode(code);
		resp.setData(null);
		return JSONUtil.toJson(resp);
	}
	
	
	protected String createJSONResponse(Object data){
		JSONResponse resp = new JSONResponse();
		resp.setStatus(true);
		resp.setCode(0);
		resp.setData(data);
		return JSONUtil.toJson(resp);
	}
}
