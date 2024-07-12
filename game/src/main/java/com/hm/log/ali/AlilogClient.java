package com.hm.log.ali;

import com.aliyun.openservices.log.Client;
import com.hm.libcore.serverConfig.AliConfig;

public class AlilogClient {
    private static Client client = new Client(AliConfig.getInstance().getEndpoint(), AliConfig.getInstance().getAccessKeyId(), AliConfig.getInstance().getAccessKey());

    public static Client getClient() {
        return client;
    }
}
