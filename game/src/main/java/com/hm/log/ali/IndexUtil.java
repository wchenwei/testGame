package com.hm.log.ali;

import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.log.common.Index;
import com.aliyun.openservices.log.common.IndexKey;
import com.aliyun.openservices.log.common.IndexKeys;
import com.aliyun.openservices.log.common.IndexLine;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetIndexRequest;
import com.aliyun.openservices.log.request.UpdateIndexRequest;
import com.aliyun.openservices.log.response.GetIndexResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.libcore.serverConfig.AliConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.log.AliLogType;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class IndexUtil {
    private static Set<String> queryIndex(String logstore) {
        try {
            GetIndexRequest request = new GetIndexRequest(AliConfig.getInstance().getProjectName(), logstore);
            GetIndexResponse response = AlilogClient.getClient().GetIndex(request);
            if (response != null) {
                IndexKeys keys = response.GetIndex().GetKeys();
                return keys.GetKeys().keySet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Sets.newHashSet();
    }

    private static void createIndex(String logstore, IndexKeys indexKeys) {
        if (indexKeys.GetKeys().size() > 0) {
            IndexLine indexLine = new IndexLine(new ArrayList<String>(), false);
            Index index = new Index(7, indexKeys, indexLine);

            UpdateIndexRequest updateIndex = new UpdateIndexRequest(AliConfig.getInstance().getProjectName(), logstore, index);
            try {
                AlilogClient.getClient().UpdateIndex(updateIndex);
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkAndCreateIndex() {
        if (StrUtil.isEmpty(SpringUtil.getBean(AliConfig.class).getProjectName())) {
            return;
        }
        ArrayList<String> tokens = Lists.newArrayList(",", ".", "#");
        for (AliLogType logType : AliLogType.values()) {
            String logstore = logType.getName();
            Map<String, String> indexMap = logType.getIndexMap();
            Set<String> indexs = IndexUtil.queryIndex(logstore);
            IndexKeys indexKeys = new IndexKeys();
            boolean isChange = false;
            for (Map.Entry<String, String> entry : indexMap.entrySet()) {
                indexKeys.AddKey(entry.getKey(), new IndexKey(tokens, false, entry.getValue()));
                if (!indexs.contains(entry.getKey())) {
                    isChange = true;
                }
            }
            if (isChange) {
                createIndex(logstore, indexKeys);
            }
        }
    }
}
