package com.hm.chsdk.event2.pack;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ZipUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.actor.IRunner;
import com.hm.chsdk.CHSendClient;
import com.hm.chsdk.ICHEvent;
import com.hm.chsdk.event2.CHObserverBiz2;
import com.hm.chsdk.event2.CHSDKContants2;

import java.util.List;

/**
 * 打包数据
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/24 18:18
 */
public class CHEventPack implements IRunner {
    public List<String> eventList = Lists.newArrayList();
    public long createTime;
    public int sendNum;


    public CHEventPack() {
        this.createTime = System.currentTimeMillis();
    }

    public void addEvent(ICHEvent event) {
        String json = GSONUtils.ToJSONString(event);
        String data = Base64.encode(json);
        this.eventList.add(data);
    }

    public boolean isFull() {
        return this.eventList.size() >= CHObserverBiz2.PackMaxNum;
    }


    public void sendPack() {
        if (CollUtil.isEmpty(this.eventList)) {
            return;
        }
        CHSendClient.sendNewJsonPost(CHSDKContants2.EventListUrl, GSONUtils.ToJSONString(eventList));
    }

    public void sendZipPack() {
        if (CollUtil.isEmpty(this.eventList)) {
            return;
        }
        String data = GSONUtils.ToJSONString(eventList);
        byte[] zipdata = ZipUtil.zlib(data, "utf-8", 3);
        CHSendClient.sendNewJsonPost(CHSDKContants2.EventListZipUrl, array2String(zipdata));
    }

    @Override
    public Object runActor() {
        if (!CHObserverBiz2.isOpen || this.sendNum >= 3) {
            return null;
        }
        this.sendNum++;
        if (CHObserverBiz2.isZip) {
            sendZipPack();
        } else {
            sendPack();
        }
        return null;
    }

    public static String array2String(byte[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(",");
        }
    }


    public static void main(String[] args) {
        List<String> dataList = Lists.newArrayList();
        String data = "eyJnIjoiMTA0MyIsInBmaWQiOiIxMDQyIiwiY2lkIjoid2luZG93cyIsInNpZCI6IndpbmRvd3MiLCJzcGZpZCI6IjEwNDMxMDQyMjAyMDAzMTMxMDU0MDQ4MSIsInBmdWlkIjoiMjAiLCJibiI6IjIwMjAwMTAyMTc0MSIsInNuIjoiMTE5NDY3MDM4QTJFMDZENkJGMzJDNkM0OTZBQzU4OTUiLCJ2IjoiMTAwMjAwOSIsInRzIjoiMTY0ODExNTQ3MzYyMyIsIm1hYyI6IndpbmRvd3MiLCJldmVudEpzb24iOiJ7XCJzekFjY291bnRcIjpcIndpbmRvd3NcIixcIkNoYXJNYWNcIjpcIndpbmRvd3NcIixcIkNoYXJJcFwiOlwiMTkyLjE2OC4xLjk2XCIsXCJQcm9mZXNzaW9uX0lkXCI6MyxcIlBob25lX1R5cGVcIjpcIndpbmRvd3NcIixcImd2XCI6XCJcIixcInRkXCI6XCIyMDIyLTAzLTI0IDE3OjUxOjEzXCIsXCJldmVudF90eXBlX2lkXCI6XCIwMDNcIixcImV2ZW50X3R5cGVfbmFtZVwiOlwi55m76ZmG55u45YWzXCIsXCJldmVudF9pZFwiOlwiMzAwMVwiLFwiZXZlbnRfbmFtZVwiOlwi55m75b2VXCIsXCJmcm9udF9ldmVudF9pZFwiOlwiMVwiLFwiY2hhbm5lbF9jcF9pZFwiOjExNCxcInNlcnZlcl9jcF9pZFwiOjYsXCJzZXJ2ZXJfY3BfbmFtZVwiOlwi5Y";
        for (int i = 0; i < 100; i++) {
            dataList.add(data);
        }
        String info = GSONUtils.ToJSONString(dataList);
        byte[] zipdata = ZipUtil.zlib(info, "utf-8", 3);
        long now = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ZipUtil.zlib(info, "utf-8", 3);
        }
        System.out.println("time " + (System.currentTimeMillis() - now));

        System.out.println(zipdata.length);

        zipdata = ZipUtil.gzip(GSONUtils.ToJSONString(dataList), "utf-8");
        System.out.println(zipdata.length);
    }
}
