package com.hm.libcore.util.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.hm.libcore.util.compression.Gzip;

import java.util.Map;

public class JackSonUtils {
    public static ObjectMapper mapper = ObjectMapperBuilder.buildJacksonObjectMapper();

    public static <T> Map<String, Object> byteToObjMap(Map<String, byte[]> hashMapped, boolean isZip) {
        Map<String, Object> objectMap = Maps.newHashMap();
        for (Map.Entry<String, byte[]> entry : hashMapped.entrySet()) {
            objectMap.put(entry.getKey(), byteToObj(entry.getValue(), isZip));
        }
        return objectMap;
    }

    public static Map<String, byte[]> objToByteMap(Map<String, Object> hashMapped, boolean isZip) {
        Map<String, byte[]> byteMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : hashMapped.entrySet()) {
            Object value = entry.getValue();
            byteMap.put(entry.getKey(), objToByte(value, isZip));
        }
        return byteMap;
    }

    public static <T> T byteToObj(byte[] data, boolean compress) {
        try {
            if (data == null || data.length <= 0) {
                return null;
            }
            return mapper.reader().forType(Object.class).readValue(compress ? Gzip.unGZip(data) : data);
        } catch (Exception e) {
            System.err.println("byteToObj出错");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] objToByte(Object obj, boolean compress) {
        byte[] data = objToByte(obj);
        if (data == null) {
            return null;
        }
        return compress ? Gzip.gZip(data) : data;
    }


    public static byte[] objToByte(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String objToString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T byteToObj(String json) {
        try {
            return mapper.reader().forType(Object.class).readValue(json);
        } catch (Exception e) {
            System.err.println("json出错:" + json);
            e.printStackTrace();
        }
        return null;
    }
}
