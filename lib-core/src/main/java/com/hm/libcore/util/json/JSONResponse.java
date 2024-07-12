package com.hm.libcore.util.json;


/**
 * Title: JSONResponse.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author 叶亮
 * @version 1.0
 * @date 2015年7月21日 下午3:03:20
 */
public class JSONResponse {
    private boolean status;//响应状态 false 表示错误信息，需要提取code
    private int code;//响应状态码，只有在status = false时才会有
    private Object data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}