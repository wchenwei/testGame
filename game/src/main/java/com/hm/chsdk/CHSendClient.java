package com.hm.chsdk;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.actor.IRunner;
import com.hm.actor.ActorDispatcherType;
import com.hm.chsdk.event2.CHObserverBiz2;
import com.hm.libcore.ok.HttpRequestMap;

import java.util.Arrays;
import java.util.List;

public class CHSendClient {
	public static boolean isPoolSend = true;

    public static void sendData(ICHEvent event) {
		String json = GSONUtils.ToJSONString(event);
		if(CHSDKContants.showLog) {
			System.err.println(event.buildUrl());
			System.err.println(json);
		}
		String data = Base64.encode(json);
		if(CHSDKContants.showLog) {
			System.err.println(data);
		}
        ActorDispatcherType.CHEventSend.putTask(new IRunner() {
			@Override
            public Object runActor() {
				if (event.isUploadEvent2() && !CHObserverBiz2.isOpen) {
					return null;
				}
                sendNewJsonPost(event.buildUrl(), data);
                return null;
			}
		});
	}

    public static boolean sendNewJsonPost(String strURL, String params) {
        return sendNewJsonPost(strURL, params, 1);
    }

    public static boolean sendNewJsonPost(String strURL, String params, int num) {
		try {
			long startTime = System.currentTimeMillis();

			String result = HttpRequestMap.create(strURL)
					.mediaType("application/x-www-form-urlencoded")
					.putKeyVal("data", params)
					.sendPost();
//			String result = HttpRequest.post(strURL)
//					.contentType("application/x-www-form-urlencoded")
//                    .timeout(300)
//					.form("data", params)
//					.execute().body();
			if(CHSDKContants.showLog) {
				System.err.println((System.currentTimeMillis()-startTime)+"ms="+result);
			}
			JSONObject jsonObj = JSONObject.parseObject(result);
			if(jsonObj.containsKey("code") && jsonObj.getIntValue("code")==200) {
				return true;
			}
            return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}

    public static void main(String[] args) {
        String data = "eyJnIjoiMTA0MyIsInBmaWQiOiIxMDQyIiwiY2lkIjoid2luZG93cyIsInNpZCI6IndpbmRvd3MiLCJzcGZpZCI6IjEwNDMxMDQyMjAyMDAzMTMxMDU0MDQ4MSIsInBmdWlkIjoiMjAiLCJibiI6IjIwMjAwMTAyMTc0MSIsInNuIjoiMTE5NDY3MDM4QTJFMDZENkJGMzJDNkM0OTZBQzU4OTUiLCJ2IjoiMTAwMjAwOSIsInRzIjoiMTY0ODExNTQ3MzYyMyIsIm1hYyI6IndpbmRvd3MiLCJldmVudEpzb24iOiJ7XCJzekFjY291bnRcIjpcIndpbmRvd3NcIixcIkNoYXJNYWNcIjpcIndpbmRvd3NcIixcIkNoYXJJcFwiOlwiMTkyLjE2OC4xLjk2XCIsXCJQcm9mZXNzaW9uX0lkXCI6MyxcIlBob25lX1R5cGVcIjpcIndpbmRvd3NcIixcImd2XCI6XCJcIixcInRkXCI6XCIyMDIyLTAzLTI0IDE3OjUxOjEzXCIsXCJldmVudF90eXBlX2lkXCI6XCIwMDNcIixcImV2ZW50X3R5cGVfbmFtZVwiOlwi55m76ZmG55u45YWzXCIsXCJldmVudF9pZFwiOlwiMzAwMVwiLFwiZXZlbnRfbmFtZVwiOlwi55m75b2VXCIsXCJmcm9udF9ldmVudF9pZFwiOlwiMVwiLFwiY2hhbm5lbF9jcF9pZFwiOjExNCxcInNlcnZlcl9jcF9pZFwiOjYsXCJzZXJ2ZXJfY3BfbmFtZVwiOlwi5Y+45LqR6b6ZMeacjVwiLFwicm9sZV9jcF9pZFwiOjYwMDM0MCxcInJvbGVfY3BfbmFtZVwiOlwiW+WPuOS6kem+mTHmnI1d6ams5YuS56ys5YW5wrfpmoZcIixcImxldmVsXCI6MTAzLFwicG93ZXJcIjoxNzU5MTksXCJtb25leVwiOjAsXCJtb25leV9zeXNcIjo3Njk4LFwidmlwX2xldmVsXCI6MX0iLCJjcFJvbGVJZCI6IjYwMDM0MCIsInJvbGVOYW1lIjoiW+WPuOS6kem+mTHmnI1d6ams5YuS56ys5YW5wrfpmoYiLCJyb2xlTGV2ZWwiOiIxMDMiLCJyb2xlVmlwTGV2ZWwiOiIxIiwiY29tYmF0UG93ZXIiOiIxNzU5MTkiLCJjcFNlcnZlcklkIjoiNiIsImNwU2VydmVybmFtZSI6IjbmnI0ifQ==";
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            list.add(data);
        }
//		data = GSONUtils.ToJSONString(list);
//		System.out.println(data);
//		System.out.println(data.getBytes().length);

        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            byte[] zipdata = ZipUtil.gzip(data, "utf-8");
            Arrays.toString(zipdata);
        }
        System.out.println(System.currentTimeMillis() - time);


//		System.out.println(Arrays.toString(zipdata));
//		System.out.println(ZipUtil.unGzip(zipdata).length);

    }
}
