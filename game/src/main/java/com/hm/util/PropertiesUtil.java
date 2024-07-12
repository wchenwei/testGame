package com.hm.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.hm.model.HasObj2Ser;
import com.hm.model.SerObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;

/**
 * 属性操作工具类
 * 
 * @author lf
 * 
 */
@Slf4j
public class PropertiesUtil
{

    /**
     * 反射调用无参方法
     * 
     * @param method
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object invokeMethodWithNoParam(String method, Object obj)
            throws Exception
    {
        return obj.getClass().getMethod(method).invoke(obj);
    }

    /**
     * 获得字段 如果没有这个字段则返回NULL
     * 
     * @param cla
     * @param fName
     * @return
     */
    public static Field getField(Class cla, String fName)
    {
        try
        {
            return cla.getDeclaredField(fName);
        }
        catch (Exception e)
        {
            log.warn("反射获得字段报错 开始尝试获取父类字段:" + fName);
            try
            {
                return cla.getSuperclass().getDeclaredField(fName);
            }
            catch (Exception e1)
            {
                log.warn("从父类中反射获得字段报错:" + fName);
                return null;
            }
        }
    }

    /**
     * 浅复制属性
     * 
     * @param from
     * @param to
     * @throws Exception
     */
    public static void copyProperties(Object from, Object to)
    {
        BeanUtils.copyProperties(to, from);
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<String, Double> parsePropertiesString(String propStr)
    {
        Map<String, Double> propMap = null;

        propMap = new HashMap<String, Double>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(sub_array[0], Double.parseDouble(sub_array[1]));
            }
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<Long, Long> parsePropertiesStringLong(String propStr)
    {
        Map<Long, Long> propMap = null;

        propMap = new HashMap<Long, Long>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(Long.parseLong(sub_array[0]),
                        Long.parseLong(sub_array[1]));
            }
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<Integer, Integer> parsePropertiesStringInteger(
            String propStr)
    {
        Map<Integer, Integer> propMap = null;

        propMap = new HashMap<Integer, Integer>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(Integer.parseInt(sub_array[0]),
                        Integer.parseInt(sub_array[1]));
            }
        }

        return propMap;
    }
    
    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Table<Integer, Integer,Integer> parsePropertiesStringTable(String propStr)
    {
        Table<Integer, Integer,Integer> propMap = HashBasedTable.create(); 

        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 3)
            {
                propMap.put(Integer.parseInt(sub_array[0]),
                        Integer.parseInt(sub_array[1]),
                        Integer.parseInt(sub_array[2]));
            }
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<Integer, Integer> parsePropertiesStringIntegerToTreeMap(
            String propStr)
    {
        Map<Integer, Integer> propMap = null;

        propMap = new TreeMap<Integer, Integer>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(Integer.parseInt(sub_array[0]),
                        Integer.parseInt(sub_array[1]));
            }
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：100,200,300,400 数值当键，值为传入的value
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<Integer, Integer> parsePropertiesStringInteger(
            String propStr, int value)
    {
        Map<Integer, Integer> propMap = null;

        propMap = new HashMap<Integer, Integer>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            propMap.put(Integer.parseInt(array_prop_str[j]), value);
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<String, Object> parsePropertiesToStringObject(
            String propStr)
    {
        Map<String, Object> propMap = null;

        propMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(sub_array[0], sub_array[1]);
            }
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<String, Integer> parsePropertiesToStringInt(String propStr)
    {
        Map<String, Integer> propMap = null;

        propMap = new HashMap<String, Integer>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(sub_array[0], Integer.parseInt(sub_array[1]));
            }
        }

        return propMap;
    }

    public static Map<String, Long> parsePropertiesToStringLong(String propStr)
    {
        Map<String, Long> propMap = null;

        propMap = new HashMap<String, Long>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(sub_array[0], Long.parseLong(sub_array[1]));
            }
        }

        return propMap;
    }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<Integer, String> parsePropertiesToIntString(String propStr)
    {
        Map<Integer, String> propMap = null;

        propMap = new HashMap<Integer, String>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(Integer.parseInt(sub_array[0]), sub_array[1]);
            }
        }

        return propMap;
    }

    /**
     * 将合并字符串中的属性设置进map对象
     * 
     * @param propStr
     * @param obj
     */
    public static void setProperties(String propStr, Map<String, Object> map)
            throws Exception
    {

        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                if (sub_array[1].equals("null"))
                {
                    sub_array[1] = null;
                }
                else
                {
                    sub_array[1] = URLDecoder.decode(sub_array[1], "utf-8");
                }
                map.put(sub_array[0], sub_array[1]);
            }
        }

    }

    public static String parserToStr(Map<String, Object> map)
    {
        String str = "";
        for (Map.Entry<String, Object> m : map.entrySet())
        {
            if (m.getValue() == null)
            {
                str += m.getKey() + ":" + ",";
            }
            else
            {
                str += m.getKey() + ":" + m.getValue() + ",";
            }
        }
        str = StringUtil.subEndDouhao(str);
        return str;
    }

    public static String parserIntegerListToStr(List<Integer> list)
    {
        String str = "";
        for (int value : list)
        {
            str += value + ",";
        }
        str = StringUtil.subEndDouhao(str);
        return str;
    }

    /**
     * 将MAP中的属性设置进对象
     * 
     * @param propMap
     * @param obj
     */
    public static void setProperties(Map<String, Double> propMap, Object obj)
            throws Exception
    {

        Set<String> propNameSet = propMap.keySet();
        for (String propName : propNameSet)
        {
            Field f = getField(obj.getClass(), propName);
            if (f != null && f.getName().equalsIgnoreCase(propName))
            {
                f.setAccessible(true);
                f.set(obj, propMap.get(propName));
            }
        }
    }

    /**
     * 根据MAP中的属性对对象中相同的属性进行增操作
     * 
     * @param propMap
     * @param obj
     */
    public static void changeProperties(Map<String, Integer> propMap, Object obj)
            throws Exception
    {
        Set<String> propNameSet = propMap.keySet();
        for (String propName : propNameSet)
        {
            Field f = getField(obj.getClass(), propName);
            if (f != null && f.getName().equalsIgnoreCase(propName))
            {
                f.setAccessible(true);
                int oldVal = f.getInt(obj);
                int newVal = propMap.get(propName);
                int val = oldVal + newVal;
                if (val < 0)
                {
                    val = 0;
                }
                f.set(obj, val);
            }
        }

    }

    /**
     * 根据MAP中的属性对对象中相同的属性进行减操作
     * 
     * @param propMap
     * @param obj
     */
    public static void minusProperties(Map<String, Double> propMap, Object obj)
            throws Exception
    {
        Set<String> propNameSet = propMap.keySet();
        for (String propName : propNameSet)
        {
            Field f = getField(obj.getClass(), propName);
            if (f != null && f.getName().equalsIgnoreCase(propName))
            {
                f.setAccessible(true);
                double oldVal = f.getDouble(obj);
                double newVal = propMap.get(propName);
                double val = oldVal - newVal;
                if (val < 0)
                {
                    val = 0;
                }
                f.set(obj, val);
            }
        }
    }

    /**
     * 根据字段名从对象中获得它的值
     * 
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object getValFromObj(String fieldName, Object obj)
            throws Exception
    {
        Field f = getField(obj.getClass(), fieldName);
        if (f == null)
        {
            return "0";
        }
        f.setAccessible(true);
        return f.get(obj);
    }

    /**
     * 根据字段名向对象中设置该字段的值
     * 
     * @param fieldName
     * @param obj
     * @return
     */
    public static void setValToObj(String fieldName, Object obj, Object val)
            throws Exception
    {
        Field f = getField(obj.getClass(), fieldName);
        if (f == null)
        {
            return;
        }
        f.setAccessible(true);
        f.set(obj, val);
    }

    /**
     * 解析属性字符串(转化为Map<String, String>)
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<String, String> parsePropertiesToString(String propStr)
    {
        Map<String, String> propMap = new HashMap<String, String>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        propStr = propStr.replaceAll("，", ",");
        propStr = propStr.replaceAll("：", ":");
        propStr = propStr.trim();
        String[] array_prop_str = propStr.split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                String[] sp_array = sub_array[1].split("_");
                if (sp_array.length > 2)
                {
                    String key = sub_array[0] + "_" + sp_array[0];
                    String value = sp_array[1] + "_" + sp_array[2];
                    propMap.put(key, value);
                    continue;
                }
                propMap.put(sub_array[0], sub_array[1]);
            }
        }

        return propMap;
    }

    /**
     * 解析Map<String,Double> 转换为以XXXX:XXXX,XXXX:XXXX格式的字符串
     * 
     * @param map
     * @return
     */
    public static String patseMapToString(Map<String, Double> map)
    {
        String str = "";
        for (Map.Entry<String, Double> entry : map.entrySet())
        {
            str += entry.getKey() + ":" + entry.getValue() + ",";
        }
        String returnStr = StringUtil.subEndDouhao(str);
        return returnStr;
    }

    /**
     * 解析Map<Long,Long> 转换为以XXXX:XXXX,XXXX:XXXX格式的字符串
     * 
     * @param map
     * @return
     */
    public static String patseMapLongToString(Map<String, Long> map)
    {
        String str = "";
        for (Map.Entry<String, Long> entry : map.entrySet())
        {
            str += entry.getKey() + ":" + entry.getValue() + ",";
        }
        String returnStr = StringUtil.subEndDouhao(str);
        return returnStr;
    }

    /**
     * 解析Map<Long,Long> 转换为以XXXX:XXXX,XXXX:XXXX格式的字符串
     * 
     * @param map
     * @return
     */
    public static String patseMapIntegerToString(Map<String, Long> map)
    {
        String str = "";
        for (Map.Entry<String, Long> entry : map.entrySet())
        {
            str += entry.getKey() + ":" + entry.getValue() + ",";
        }
        String returnStr = StringUtil.subEndDouhao(str);
        return returnStr;
    }

    /**
     * 解析Map<Integer,Integer> 转换为以XXXX:XXXX,XXXX:XXXX格式的字符串
     * 
     * @param map
     * @return
     */
    public static String parseMapIntegerToString(Map<Integer, Integer> map)
    {
        String str = "";
        for (Map.Entry<Integer, Integer> entry : map.entrySet())
        {
            str += entry.getKey() + ":" + entry.getValue() + ",";
        }
        String returnStr = StringUtil.subEndDouhao(str);
        return returnStr;
    }
    
    /**  
     * parseMapIntegerStringToString:解析Map<Integer,String> 转换为以XXXX:XXXX,XXXX:XXXX格式的字符串. <br/>  
     * TODO(这里描述这个方法适用条件 – 可选).<br/>  
     *  
     * @author zqh  
     * @param map
     * @return  使用说明
     *
     */
    public static String parseMapIntegerStringToString(Map<Integer, String> map)
    {
        String str = "";
        for (Map.Entry<Integer, String> entry : map.entrySet())
        {
            str += entry.getKey() + ":" + entry.getValue() + ",";
        }
        String returnStr = StringUtil.subEndDouhao(str);
        return returnStr;
    }

    /**
     * 判断指定数是否在字符串数字范围中 如 5 是否在 1-10 返回true 15 是否在 1-10 返回false
     * 
     * @param rangeStr
     *            数字范围格式为 “最小数-最大数”
     * @param num
     * @return
     */
    public static boolean inRange(String rangeStr, double num)
    {
        if (rangeStr == null || rangeStr.trim().length() == 0)
        {
            return false;
        }
        String[] rangeArr = rangeStr.split("-");
        if (rangeArr.length > 1)
        {
            if (Double.parseDouble(rangeArr[0]) <= num
                    && Double.parseDouble(rangeArr[1]) >= num)
            {
                return true;
            }
        }
        return false;
    }

    // /**
    // * 字符串json转Map
    // *
    // * @param s
    // * @return
    // * @throws Exception
    // */
    // public static Map<String, Object> parserToMap(String s) throws Exception{
    // Map<String, Object> map = new HashMap<String, Object>();
    // map = parserToObj(s, map.getClass());
    // if(map == null){
    // return Collections.emptyMap();
    // }
    // return map;
    // }
    //
    // /**
    // * 字符串json转对象
    // *
    // * @param s
    // * @return
    // * @throws Exception
    // */
    // public static <T>T parserToObj(String s, Class<T> clazz) throws
    // Exception{
    // // if(StringUtil.isNotBlank(s)){
    // // if( s.startsWith("{") && s.endsWith("}")){
    // // return JSONUtil.toObj(s, clazz);
    // // }
    // // }
    // return null;
    // }

    /**
     * 解析属性字符串
     * 
     * @param propStr
     *            原始属性字符串 格式：属性名：值；属性名：值 如：tiLi:-1,zhiHui:1,money:-10
     * @return 属性MAP,key为属性名，value为属性值
     */
    public static Map<Integer, Integer> parsePropertiesToIntegerInteger(
            String propStr)
    {
        Map<Integer, Integer> propMap = new HashMap<Integer, Integer>();
        if (StringUtils.isBlank(propStr))
        {
            return propMap;
        }
        String[] array_prop_str = propStr.trim().split(",");
        for (int j = 0; j < array_prop_str.length; j++)
        {
            String[] sub_array = array_prop_str[j].split(":");
            if (sub_array.length == 2)
            {
                propMap.put(Integer.parseInt(sub_array[0]),
                        Integer.parseInt(sub_array[1]));
            }
        }

        return propMap;
    }

    public static List<Byte> parseStringToByteList(String propStr)
    {
        List<Byte> list = new ArrayList<Byte>();
        String[] proArr = propStr.split(",");
        for (String item : proArr)
        {
            list.add(Byte.parseByte(item));
        }
        return list;

    }

    public static Set<Integer> parseStringToIntegerList(String propStr)
    {
        Set<Integer> list = Sets.newHashSet();
        if (StringUtils.isBlank(propStr))
        {
            return list;
        }
        String[] proArr = propStr.split(",");
        for (String item : proArr)
        {
            list.add(Integer.parseInt(item));
        }
        return list;

    }

    /**
     * 序列化某个对象A中的内置对象B到对象A上的字节数组属性
     * 
     * @param obj
     * @throws Exception
     */
    public static void setSerObj2ByteArr(HasObj2Ser obj) throws Exception
    {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj.getSerObject());
            obj.setSerObjectArr(bos.toByteArray());
        }
        finally
        {
            if (bos != null)
            {
                bos.close();
            }
            if (oos != null)
            {
                oos.close();
            }
        }
    }

    /**
     * 从某个对象A中的字节数组属性中反序列化得到对象
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static void setSerObjFromByteArr(HasObj2Ser obj) throws Exception
    {
        byte[] arr = obj.getSerObjectArr();
        if (arr == null || arr.length == 0)
        {
            return;
        }
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(new ByteArrayInputStream(arr));
            SerObject serObject = (SerObject) ois.readObject();
            obj.setSerObject(serObject);
        }
        finally
        {
            if (ois != null)
            {
                ois.close();
            }
        }
    }

}
