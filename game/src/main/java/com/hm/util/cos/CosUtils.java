package com.hm.util.cos;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.pro.ProUtil;
import com.hm.libcore.util.thread.ThreadPoolUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.internal.SkipMd5CheckStrategy;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Description: 腾讯对象文件上传,用于存储大文件
 * @author siyunlong  
 * @date 2019年10月25日 下午3:31:13 
 * @version V1.0
 */
@Slf4j
public class CosUtils {
	private static ExecutorService cachedThreadPool = ThreadPoolUtils.buildExecutorService(2, 8, "cos-upload");;
	private static COSClient cosClient;
	public static String bucketName;

	static {
		initClient();
	}

	public synchronized static void initClient() {
		if(cosClient != null) {
			return;
		}
		bucketName = KeyCosUtils.bucketName;
		if(StrUtil.isEmpty(KeyCosUtils.bucketName)) {
			return;
		}
		//跳过MD5校验
		System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, "1");
		// 1 初始化用户身份信息（secretId, secretKey）。
		String secretId = KeyCosUtils.secretId;
		String secretKey = KeyCosUtils.secretKey;
		COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
		// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
		Region region = new Region(KeyCosUtils.region);
		ClientConfig clientConfig = new ClientConfig(region);
		// 3 生成 cos 客户端。
		cosClient = new COSClient(cred, clientConfig);
		log.error("Cos初始化成功!!");
	}


	public static COSClient getCosClient() {
		return cosClient;
	}

	public static void setCosClient(COSClient cosClient) {
		CosUtils.cosClient = cosClient;
	}

	public static void uploadShowTime(String id,Object obj) {
		if(cosClient == null) {
			return;
		}
		long now = System.currentTimeMillis();
		try {
			Future<Integer> result = cachedThreadPool.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					upload(id, obj);
					return 1;
				}
			});
//			try {
//				//等待1秒
//				result.get(1L, TimeUnit.SECONDS);//等待1秒回应
//			} catch (Exception e) {
//            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			log.info(id+"=新COS上传耗时:"+(System.currentTimeMillis()-now)+"ms");
		}
	}

	private static void upload(String id,Object obj) {
		String content = GSONUtils.ToJSONString(obj);
		String key = id+".zip";
		try {
			byte[] contentByteArray = ZipUtil.gzip(content.getBytes(StringUtils.UTF8));
//			byte[] contentByteArray = content.getBytes(StringUtils.UTF8);
			InputStream contentInput = new ByteArrayInputStream(contentByteArray);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("text/plain");
			metadata.setContentLength(contentByteArray.length);
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, contentInput,metadata);
			PutObjectResult result = cosClient.putObject(putObjectRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}