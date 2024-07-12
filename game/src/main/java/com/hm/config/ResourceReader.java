package com.hm.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ExcleConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	public static final String CONFPATH = getSystemPath()+"/config/json/";
	public static final String ZIPPATHName = getSystemPath() + "/config/json.zip";


	public static String getSystemPath() {
		String path = System.getProperty( "catalina.base" );
		if(StrUtil.isNotEmpty(path)) {
			return path;
		}
		return System.getProperty( "user.dir" );
	}

	private Map<String, ExcleConfig> configMap = Maps.newConcurrentMap();

	private ResourceReader() {
		try {
			Map<String, ExcleConfig> maps = SpringUtil.getBeanMap(ExcleConfig.class);
			for (ExcleConfig config : maps.values()) {
				for (String fileName : config.getDownloadFile()) {
					this.configMap.put(fileName, config);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExcleConfig getExcleConfig(String fileName) {
		return this.configMap.get(fileName);
	}

	/**
	 * 下载所有配置文件
	 */
	public void downloadAllProp() {
		String jsonUrl = ServerConfig.getInstance().getDownloadUrl();
		if (StrUtil.endWith(jsonUrl, ".zip")) {
			downloadAllPropForZip();
		} else {
			downloadAllPropForManyFile();
		}
	}

	public void downloadAllPropForManyFile() {
		try {
			List<String> readLines = Lists.newArrayList(this.configMap.keySet());
			downloadProp(readLines);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadAllPropForZip() {
		try {
			String jsonZipUrl = ServerConfig.getInstance().getDownloadUrl();
			System.err.println("json:" + jsonZipUrl);
			final long oldSize = FileUtil.file(ZIPPATHName).length();
			System.err.println("old zip len:" + oldSize);
			FileUtil.del(ZIPPATHName);

			CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
			client.start();

			final CountDownLatch latch = new CountDownLatch(1);
			final HttpGet httpget = new HttpGet(jsonZipUrl);
			client.execute(httpget, new FutureCallback<HttpResponse>() {
				@Override
				public void completed(HttpResponse response) {
					try {
						System.err.println("===========下载json.zip成功==========");
						HttpEntity entity = response.getEntity();
						InputStream is = entity.getContent();
						File file = new File(ZIPPATHName);
						file.getParentFile().mkdirs();
						FileOutputStream os = new FileOutputStream(file);
						ByteStreams.copy(is, os);
						is.close();
						os.close();
						long newZipLen = file.length();
						System.err.println("new zip len:" + file.length());
						if (newZipLen == oldSize) {
							System.err.println("!!!!!!!!!!!json zip 没有更改!!!!!!!!!!!!!!!!!");
						}
						System.err.println("===========解压json.zip成功==========");
						ZipUtil.unzip(file);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					latch.countDown();
				}

				@Override
				public void failed(Exception ex) {
					latch.countDown();
					System.err.println("下载json.zip失败->" + ex.getLocalizedMessage());
				}

				@Override
				public void cancelled() {
					latch.countDown();
					System.err.println("下载json.zip->cancelled");
				}
			});
			latch.await();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载更新配置文件
	 * @return
	 */
	public List<String> updateServerProp() {
		try {
			CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
			client.start();
			String url = ServerConfig.getInstance().getDownloadUrl();
			List<String> readLines = getFileList(client, url + "update.txt");
			final CountDownLatch latch = new CountDownLatch(readLines.size());
			for (String readLine : readLines) {
				download(client, url, dealFileName(readLine), latch);
			}
			latch.await();
			client.close();
			return readLines;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Lists.newArrayList();
	}

	private void downloadProp(List<String> readLines) throws Exception {
		String url = ServerConfig.getInstance().getDownloadUrl();
		CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
		client.start();
		final CountDownLatch latch = new CountDownLatch(readLines.size());
		for (String readLine : readLines) {
			download(client, url, dealFileName(readLine), latch);
		}
		latch.await();
		// List<String> urlList =
		// StringUtil.splitStr2StrList(ServerConfig.getInstance().getDownloadUrl(),
		// ";");
		// for (String url : urlList) {
		// List<String> readLines = getFileList(client, url + "version.txt");
		//
		// }
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
		return result+".json";
	}

	/**
	 * 下载文件
	 */
	private void download(CloseableHttpAsyncClient client, String url, final String fileName, final CountDownLatch latch){
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
					System.err.println("下载" + fileName + "失败->" + ex.getLocalizedMessage());
				}

				@Override
				public void cancelled() {
					latch.countDown();
					System.err.println("下载" + fileName + "->cancelled");
				}
			});
		} catch (Exception e) {
			System.err.println(url + fileName);
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
//		ResourceReader.getInstance().downloadAllProp();
//		String fileName = "";
//		ResourceReader.getInstance().getExcleConfig(fileName).loadConfig();
//		System.err.println(System.getProperty( "catalina.base" ));
		downloadAllPropForZip();
	}
}
