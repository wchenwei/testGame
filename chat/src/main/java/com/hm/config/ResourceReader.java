package com.hm.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.system.SystemUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.config.excel.ExcleConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Slf4j
public class ResourceReader {
    private static final ResourceReader instance = new ResourceReader();

    public static ResourceReader getInstance() {
        return instance;
    }

    public static final String CONFPATH = getSystemPath() + "/config/json/";
    public static final String ZIPPATHName = getSystemPath() + "/config/json.zip";


    public static String getSystemPath() {
        if (SystemUtil.getOsInfo().isWindows()) {
            return System.getProperty("user.dir");
        } else {
            return System.getProperty("catalina.base");
        }
    }

    private Map<String, ExcleConfig> configMap = Maps.newConcurrentMap();

    private ResourceReader() {
//        try {
//            Map<String, ExcleConfig> maps = SpringUtil.getBeanMap(ExcleConfig.class);
//            for (ExcleConfig config : maps.values()) {
//                for (String fileName : config.getDownloadFile()) {
//                    this.configMap.put(fileName, config);
//                }
//            }
//        } catch (Exception e) {
//            LogUtils.error(e);
//        }
    }

    public ExcleConfig getExcleConfig(String fileName) {
        return this.configMap.get(fileName);
    }

    /**
     * 下载所有配置文件
     */
    public void downloadAllProp() {
        downloadAllPropForZip();

//        if (ServerConfig.getInstance().isJsonIsZip()) {
//            downloadAllPropForZip();
//        } else {
//            downloadAllPropForManyFile();
//        }
    }

    public static void downloadAllPropForZip() {
        try {
            String jsonZipUrl = ServerConfig.getInstance().getDownloadUrl();
            log.info("json:" + jsonZipUrl);
            final long oldSize = FileUtil.file(ZIPPATHName).length();
            log.info("old zip len:" + oldSize);
            FileUtil.del(ZIPPATHName);

            CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
            client.start();

            final CountDownLatch latch = new CountDownLatch(1);
            final HttpGet httpget = new HttpGet(jsonZipUrl);
            client.execute(httpget, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse response) {
                    try {
                        log.info("===========下载json.zip成功==========");
                        HttpEntity entity = response.getEntity();
                        InputStream is = entity.getContent();
                        File file = new File(ZIPPATHName);
                        file.getParentFile().mkdirs();
                        FileOutputStream os = new FileOutputStream(file);
                        ByteStreams.copy(is, os);
                        is.close();
                        os.close();
                        log.info("new zip len:" + file.length());
                        log.info("===========解压json.zip成功==========");
                        ZipUtil.unzip(file);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    latch.countDown();
                }

                @Override
                public void failed(Exception ex) {
                    latch.countDown();
                    log.error("下载json.zip失败->" + ex.getLocalizedMessage());
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                    log.error("下载json.zip->cancelled");
                }
            });
            latch.await();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载所有配置文件
     */
    public void downloadAllPropForManyFile() {
        try {
            List<String> readLines = Lists.newArrayList(this.configMap.keySet());
            //入参 要扫描的包名
//            Reflections f = new Reflections("com.hg.warpath");
//            //入参 目标注解类
//            Set<String> readLines = f.getTypesAnnotatedWith(FileConfig.class)
//                    .stream().map(e -> AnnotationUtil.getAnnotationValue(e, FileConfig.class).toString())
//                    .collect(Collectors.toSet());
            downloadProp(readLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void downloadProp(Collection<String> readLines) throws Exception {
        String url = ServerConfig.getInstance().getDownloadUrl();
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        final CountDownLatch latch = new CountDownLatch(readLines.size());
        for (String readLine : readLines) {
            download(client, url, dealFileName(readLine), latch);
        }
        latch.await();
        client.close();
    }

    private List<String> getFileList(HttpAsyncClient client, String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        Future<HttpResponse> future = client.execute(httpget, null);
        HttpResponse response = future.get();
        HttpEntity entity = response.getEntity();
        InputStreamReader reader = new InputStreamReader(entity.getContent());
        List<String> result = CharStreams.readLines(reader);
        reader.close();
        return result;
    }

    private String dealFileName(String lines) {
        String result = lines.split(" ")[0];
        if (result.startsWith("﻿")) {
            result = result.substring(1);
        }
        return result + ".json";
    }

    /**
     * 下载文件
     */
    private void download(CloseableHttpAsyncClient client, String url, final String fileName, final CountDownLatch latch) {
        try {
            final HttpGet httpget = new HttpGet(url + fileName);
            client.execute(httpget, new FutureCallback<HttpResponse>() {

                @Override
                public void completed(HttpResponse response) {
                    try {
                        HttpEntity entity = response.getEntity();
                        InputStream is = entity.getContent();
                        File file = new File(CONFPATH + fileName);
                        file.getParentFile().mkdirs();
                        FileOutputStream os = new FileOutputStream(file);
                        ByteStreams.copy(is, os);
                        is.close();
                        os.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    latch.countDown();
                }

                @Override
                public void failed(Exception ex) {
                    latch.countDown();
                    log.error("下载" + fileName + "失败->" + ex.getLocalizedMessage());
                }

                @Override
                public void cancelled() {
                    latch.countDown();
                    log.error("下载" + fileName + "->cancelled");
                }
            });
        } catch (Exception e) {
            latch.countDown();
            log.error("", e);
        }
    }

}

