package com.hm.action.cmq;

import cn.hutool.json.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;

public class CmqMsg {
    private int msgId;
    private Map<String, Object> paramMap = Maps.newHashMap();

    public CmqMsg(int msgId) {
        this.msgId = msgId;
    }

    public CmqMsg(String msgBody) {
        JSONObject msg = new JSONObject(msgBody);
        this.msgId = msg.getInt("msgId");
        this.paramMap = (Map<String, Object>) msg.getJSONObject("paramMap");
    }

    public static CmqMsg create(int msgId) {
        return new CmqMsg(msgId);
    }

    public int getMsgId() {
        return msgId;
    }

    public boolean containsKey(String key) {
        return this.paramMap.containsKey(key);
    }

    public void addProperty(String key, Object value) {
        if (value == null) {
            return;
        }
        this.paramMap.put(key, value);
    }

    public String getString(String key) {
        try {
            return this.paramMap.get(key).toString();
        } catch (Exception e) {
        }
        return null;
    }

    public int getInt(String key) {
        try {
            return (int) this.paramMap.get(key);
        } catch (Exception e) {
        }
        return 0;
    }

    public boolean getBoolean(String key) {
        try {
            return (boolean) this.paramMap.get(key);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return false;
    }

    public long getLong(String key) {
        try {
            return Long.valueOf(String.valueOf(this.paramMap.get(key))).longValue();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return 0;
    }

    public Object getObject(String key) {
        try {
            return this.paramMap.get(key);
        } catch (Exception e) {
        }
        return null;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("messgeId:").append(msgId).append(":");
        paramMap.entrySet().forEach(t -> {
            sb.append(t.getKey()).append(":").append(t.getValue()).append(",");
        });
        return sb.toString();
    }
}
