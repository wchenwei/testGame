package com.hm.log.ali;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.aliyun.openservices.log.common.LogItem;

import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.serverConfig.AliConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 阿里云日志采集
 *
 * @author siyun
 */
@Slf4j
public class AliLogProducerUtil {

    private static String projectName;
    private static LogProducer producer;

    public static void init() {
        projectName = AliConfig.getInstance().getProjectName();
        // 使用默认producer配置
        if (StrUtil.isEmpty(projectName)) {
            return;
        }

        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setLingerMs(1000);//1s检查一次
        producerConfig.setIoThreadCount(8);//同时启动8个线程调用
        /**
         * 如果 producer 可用空间不足，调用者在 send 方法上的最大阻塞时间，默认为 60 秒。
         如果超过这个时间后所需空间仍无法得到满足，send 方法会抛出 TimeoutException。
         如果将该值设为0，当所需空间无法得到满足时，send 方法会立即抛出 TimeoutException。
         如果您希望 send 方法一直阻塞直到所需空间得到满足，可将该值设为负数。
         */
        producerConfig.setMaxBlockMs(0);
        /**
         * 当一个 ProducerBatch 中缓存的日志条数大于等于 batchCountThreshold 时，该 batch 将被发送，默认为 4096，最大可设置成 40960。
         */
        producerConfig.setBatchCountThreshold(100);
        producer = new LogProducer(producerConfig);
        // 添加多个project配置
        producer.putProjectConfig(buildProjectConfig());
        System.err.println("================ali projectName:" + projectName);
    }

    public static ProjectConfig buildProjectConfig() {
        return new ProjectConfig(AliConfig.getInstance().getProjectName(), AliConfig.getInstance().getEndpoint(), AliConfig.getInstance().getAccessKeyId(), AliConfig.getInstance().getAccessKey());
    }

    public static void sendLog(String logstore, List<LogItem> logItems) {
        try {
            if (producer == null || CollUtil.isEmpty(logItems)) {
                return;
            }
            //source =""  解决发送请求不合并造成的线程堵塞
            producer.send(projectName, logstore, logItems);
        } catch (Exception e) {
            log.error("Logstore:" + logstore  + ",logItems:" + JSONUtil.toJson(logItems) + "发送异常", e);
        }
    }

    public static void close() {
        try {
            if (producer != null) {
                producer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LogProducer getProducer() {
        return producer;
    }

    public static void setProducer(LogProducer producer) {
        AliLogProducerUtil.producer = producer;
    }
}
