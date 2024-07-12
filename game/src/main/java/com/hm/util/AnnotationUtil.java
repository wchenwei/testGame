package com.hm.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.observer.ObservableEnum;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName AnnotationUtil
 * @Deacription 获取注解的所有类
 * @Author zxj
 * @Date 2022/3/3 9:23
 * @Version 1.0
 **/
public class AnnotationUtil {

    public static Map<ObservableEnum, List<Class>> getClassAnnoType(String pack, String annoType){
        return getClassPackage(pack, annoType, AnnoTypeEnum.cl);
    }
    public static Map<ObservableEnum, List<Class>> getMethodAnnoType(String pack, String annoType){
        return getClassPackage(pack, annoType, AnnoTypeEnum.met);
    }
    public static Map<ObservableEnum, List<Class>> getAnnoTypeAll(String pack, String annoType){
        return getClassPackage(pack, annoType, AnnoTypeEnum.all);
    }
    private static Map<ObservableEnum, List<Class>> getClassPackage(String pack, String annoType, AnnoTypeEnum type) {
        Map<ObservableEnum, List<Class>> resultMap = Maps.newHashMap();
        Set<Class<?>> result = Sets.newHashSet();
        // 包下面的类
        Set<Class<?>> clazzs = getClasses(pack);
        if (clazzs == null) {
            return null;
        }
        System.out.printf("========"+clazzs.size() + "");
        for (Class<?> clazz : clazzs) {
            // 获取类上的注解
            if(AnnoTypeEnum.cl==type || AnnoTypeEnum.all==type) {
                Annotation[] annos = clazz.getAnnotations();
                Arrays.stream(annos).filter(e->e.annotationType().getName().equals(annoType)).forEach(e->{
                    EventMsg tmpAno = (EventMsg) e;
                    List tempList = resultMap.getOrDefault(tmpAno.obserEnum(), Lists.newArrayList());
                    tempList.add(clazz);
                    resultMap.put(tmpAno.obserEnum(), tempList);
                    //System.out.println("========"+clazz.getSimpleName().concat(".").concat(anno.annotationType().getSimpleName()));
                });
            }else if(AnnoTypeEnum.met==type || AnnoTypeEnum.all==type) {
                // 获取方法上的注解
                Method[] methods = clazz.getDeclaredMethods();
                Arrays.stream(methods).flatMap(e-> Arrays.stream(e.getDeclaredAnnotations()))
                        .filter(e->e.annotationType().getName().equals(annoType)).forEach(e->{
                            EventMsg tmpAno = (EventMsg) e;
                            List tempList = resultMap.getOrDefault(tmpAno.obserEnum(), Lists.newArrayList());
                            tempList.add(clazz);
                            resultMap.put(tmpAno.obserEnum(), tempList);
                        }
                );
            }
        }
        return resultMap;
    }

    private static Set<Class<?>> getClasses(String pack) {
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    // System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
                                                        Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' + className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(
                            Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
}

enum AnnoTypeEnum {
    cl(1, "类注解"),
    met(2, "方法注解"),
    all(3, "类和方法注解"),
    ;
    private AnnoTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    private int type;
    private String desc;
}