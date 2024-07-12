package com.hm.config.excel;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.ResourceReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class ExcleConfig {
    public abstract void loadConfig();

    public abstract List<String> getDownloadFile();

//	public <C> List<C> loadFile(Class<C> t) {
//		FileConfig fileConfig = t.getAnnotation(FileConfig.class);
//		String json = readFile(ResourceReader.CONFPATH+fileConfig.value()+".json");
//		List<C> objs = JSONUtil.fromJson(json, new TypeReference<ArrayList<C>>(){});
//		return objs;
//	}

    public <C> String getJson(Class<C> t) {
        FileConfig fileConfig = t.getAnnotation(FileConfig.class);
        String json = readFile(ResourceReader.CONFPATH + fileConfig.value() + ".json");
        return json;
    }

    public List<String> getConfigName(Class... configs) {
        List<String> names = Lists.newArrayList();
        for (Class c : configs) {
            FileConfig fileConfig = (FileConfig) c.getAnnotation(FileConfig.class);
            names.add(fileConfig.value());
        }
        return names;
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

}
