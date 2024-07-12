package com.hm.config.excel;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.annotation.PeriodsMark;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.json.JsonUtil;
import com.hm.config.ResourceReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Slf4j
public abstract class ExcleConfig{
	public abstract void loadConfig();
	public List<String> getDownloadFile() {
		return Lists.newArrayList();
	}

	@Getter
	public Set<String> fileList = Sets.newHashSet();

	@Deprecated
	public <C> String getJson(Class<C> t) {
		this.fileList.addAll(getConfigName(t));
		FileConfig fileConfig = t.getAnnotation(FileConfig.class);
		String json = readFile(ResourceReader.CONFPATH+fileConfig.value()+".json");
		return json;
	}

	public static <T> List<T> json2ListForStatic(Class<T> targetClass) {
		return json2List(targetClass, null);
	}

	public <T> List<T> json2List(Class<T> targetClass) {
		//加载
		this.fileList.addAll(getConfigName(targetClass));
		return json2List(targetClass, null);
	}

	public static <T> List<T> json2List(Class<T> targetClass, Object suffix) {
		List<T> objList = JsonUtil.jsonStr2List(getJson(targetClass, suffix), targetClass);
		try {
			List<Method> initMethods = getConfInitMethod(targetClass);
			if (CollUtil.isNotEmpty(initMethods)) {
				for (T t : objList) {
					initMethods.forEach(e -> {
						try {
							ReflectUtil.invoke(t, e);
						} catch (Exception ex) {
							System.out.println(targetClass.getSimpleName() + "初始化init出错:"+ GSONUtils.ToJSONString(t));
							ex.printStackTrace();
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objList;
	}

	public <T> List<T> json2ImmutableList(Class<T> targetClass) {
		return ImmutableList.copyOf(json2List(targetClass));
	}
	public <T, K> Map<K, T> json2Map(Function<? super T, ? extends K> keyMapper, Class<T> targetClass) {
		this.fileList.addAll(getConfigName(targetClass));
		return json2Map(keyMapper, targetClass, null);
	}

	public static <T, K> Map<K, T> json2Map(Function<? super T, ? extends K> keyMapper, Class<T> targetClass, Object suffix) {
		List<T> list = json2List(targetClass, suffix);
		return list.stream().collect(Collectors.toMap(keyMapper, e -> e));
	}
	public <T, K> Map<K, T> json2ImmutableMap(Function<? super T, ? extends K> keyMapper, Class<T> targetClass) {
		this.fileList.addAll(getConfigName(targetClass));
		return json2ImmutableMap(keyMapper, targetClass, null);
	}

	public static <T, K> Map<K, T> json2ImmutableMap(Function<? super T, ? extends K> keyMapper, Class<T> targetClass, Object suffix) {
		return ImmutableMap.copyOf(json2Map(keyMapper, targetClass, suffix));
	}

	public <T> Map<Integer, T> PeriodsJson2Map(Class<T> targetClass) {
		Map<Integer, T> resultMap = Maps.newHashMap();
		for (String fileName : findFileNamesForPeriods(getFileJsonName(targetClass))) {
			String json = readFileForJson(fileName);
			T t = json2ObjForStr(targetClass, json);
			resultMap.put(getConfPeriod(fileName), t);
		}
		return ImmutableMap.copyOf(resultMap);
	}

	public static <T> T json2Obj(Class<T> targetClass, Object suffix) {
		try {
			return json2ObjForStr(targetClass, getJson(targetClass, suffix));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public <T> T json2Obj(Class<T> targetClass) {
		return json2Obj(targetClass, null);
	}

	private static <T> T json2ObjForStr(Class<T> targetClass, String json) {
		try {
			T obj = JsonUtil.jsonStr2Object(json, targetClass);
			List<Method> initMethods = getConfInitMethod(targetClass);
			initMethods.forEach(e -> ReflectUtil.invoke(obj, e));
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<Method> getConfInitMethod(Class<?> className) {
		return Arrays.stream(ReflectUtil.getMethodsDirectly(className, false))
				.filter(e -> e.isAnnotationPresent(ConfigInit.class)).collect(Collectors.toList());
	}


	public static String getJson(Class t, Object suffix) {
		String jsonName = buildFileName(getFileJsonName(t), suffix);
		return readFileForJson(jsonName);
	}

	private static String readFileForJson(String jsonName) {
		return readFile(ResourceReader.CONFPATH + jsonName + ".json");
	}

	public List<String> getConfigName(Class... configs) {
		List<String> names = Lists.newArrayList();
		for (Class c : configs) {
			if (c.isAnnotationPresent(PeriodsMark.class)) {
				names.addAll(getPeriodsFileJsonName(c));
			} else {
				names.add(getFileJsonName(c));
			}
		}
		return names;
	}

	private static List<String> getPeriodsFileJsonName(Class c) {
		FileConfig fileConfig = (FileConfig) c.getAnnotation(FileConfig.class);
		String value = fileConfig.value();
		Integer periods = Convert.toInt(AnnotationUtil.getAnnotationValue(c, PeriodsMark.class), 1);
		List<String> list = Lists.newArrayList();
		for(int i =1; i<= periods; i++){
			list.add(buildFileName(value, i));
		}
		return list;
	}

	private static String getFileJsonName(Class c) {
		FileConfig fileConfig = (FileConfig) c.getAnnotation(FileConfig.class);
		return fileConfig.value();
	}


	public static String readFile(String path) {
		try {
			StringBuffer sb = new StringBuffer();
			File file = new File(path);
			for (String line : Files.readLines(file, Charsets.UTF_8)) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 找出jsonName为开始的所有期数配置
	 * @param jsonName
	 * @return
	 */
	private static List<String> findFileNamesForPeriods(String jsonName) {
		try {
			Pattern p = Pattern.compile(jsonName + "_\\d+$");
			return FileUtil.listFileNames(ResourceReader.CONFPATH)
					.stream()
					.filter(e -> StrUtil.endWith(e, ".json"))
					.map(e -> StrUtil.sub(e, 0, e.length() - 5))
					.filter(e -> ReUtil.contains(p, e))
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Lists.newArrayList(jsonName);
	}

	/**
	 * 按照期数默认规则  配置名_期数
	 * @param fileName
	 * @return
	 */
	private static int getConfPeriod(String fileName) {
		String[] temps = fileName.split("_");
		return Integer.parseInt(temps[temps.length - 1]);
	}

	/**
	 * 添加json文件后缀 默认 配置名_添加后缀
	 * @param jsonName
	 * @param suffix
	 * @return
	 */
	private static String buildFileName(String jsonName, Object suffix) {
		return suffix != null ? jsonName + "_" + suffix.toString() : jsonName;
	}

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("map_\\d+$");
		System.out.println(ReUtil.contains(pattern, "map_1"));
		System.out.println(ReUtil.contains(pattern, "map_21"));
		System.out.println(ReUtil.contains(pattern, "map_12cc"));
	}
}
