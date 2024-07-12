package com.hm.libcore.msg;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.json.JsonUtil;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 基础消息通讯结构
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/21 16:56
 */
@NoArgsConstructor
public class DefaultMsg {
    protected int msgId;
    protected long playerId;
    protected long createTime;
    protected Map<String, Object> paramMap = Maps.newHashMap();

    public DefaultMsg(int msgId) {
        this.msgId = msgId;
    }

    public static DefaultMsg create(int msgId) {
        return new DefaultMsg(msgId);
    }

    public int getMsgId() {
        return msgId;
    }

    public boolean containsKey(String key) {
        return this.paramMap.containsKey(key);
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void addProperty(String key, Object value) {
        if (value == null) {
            return;
        }
        this.paramMap.put(key, value);
    }

    public void addPropertyMap(Map<String, Object> paramMaps){
        if (CollUtil.isNotEmpty(paramMaps)){
            this.paramMap.putAll(paramMaps);
        }
    }

    public void addLongPropertyMap(Map<String, Long> paramMaps){
        if (CollUtil.isNotEmpty(paramMaps)){
            this.paramMap.putAll(paramMaps);
        }
    }

    public String getString(String key) {
        try {
            Object obj = this.paramMap.get(key);
            if (obj == null) {
                return null;
            }
            if (obj instanceof String) {
                return (String) obj;
            }
            return GSONUtils.ToJSONString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getInt(String key) {
        return Convert.toInt(this.paramMap.get(key),0);
    }

    public boolean hasKey(String key) {
        return this.paramMap.containsKey(key);
    }

    public boolean getBoolean(String key) {
        return Convert.toBool(this.paramMap.get(key),false);
    }

    public long getLong(String key) {
        return Convert.toLong(this.paramMap.get(key),0L);

    }

    public double getDouble(String key) {
        return Convert.toDouble(this.paramMap.get(key),0d);
    }

    public <T> T getObject(String key) {
        try {
            return (T) this.paramMap.get(key);
        } catch (Exception e) {
        }
        return null;
    }

    public <T> T getObjectFromJson(String key, Class<T> tClass) {
        try {
            Object val = this.paramMap.get(key);
            if (val == null) {
                return null;
            }
            if (val instanceof String) {
                return GSONUtils.FromJSONString((String) val, tClass);
            }
            return GSONUtils.FromJSONString(GSONUtils.ToJSONString(val), tClass);
        } catch (Exception e) {
        }
        return null;
    }

    public <T> List<T> getList(String key, Class<T> tClass) {
        try {
            return JsonUtil.obj2List(this.paramMap.get(key), tClass);
        } catch (Exception e) {
        }
        return null;
    }

    public <T> T getClientVO(Class<T> tClass) {
        try {
            String json = JsonUtil.object2JsonStr(paramMap);
            return JsonUtil.jsonStr2Object(json, tClass);
        } catch (Exception e) {
        }
        return null;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public void cloneMsg(DefaultMsg jsonMsg) {
        this.addPropertyMap(jsonMsg.getParamMap());
    }

    public String buildJsonStr() {
//                    return JsonUtil.object2JsonStr(paramMap);
        return GSONUtils.ToJSONString(paramMap);
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }
}
