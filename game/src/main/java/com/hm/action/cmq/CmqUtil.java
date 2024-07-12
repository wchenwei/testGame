package com.hm.action.cmq;


public class CmqUtil {
    private static String secretId;
    private static String secretKey;
    private static String endpoint;
    private static CmqUtil instance;
    private static String defaultQueuName;

    private CmqUtil() {

    }

    public static CmqUtil getInstance() {
        if (instance == null) {
            instance = new CmqUtil();
        }
        return instance;
    }

    public static String getSecretId() {
        return secretId;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static String getEndpoint() {
        return endpoint;
    }

    public void load(String secretId, String secretKey, String endpoint, String defaultQueueName) {
        CmqUtil.secretId = secretId;
        CmqUtil.secretKey = secretKey;
        CmqUtil.endpoint = endpoint;
        CmqUtil.defaultQueuName = defaultQueueName;
    }


    public static String getDefaultQueuName() {
        return defaultQueuName;
    }

    public static void setDefaultQueuName(String defaultQueuName) {
        CmqUtil.defaultQueuName = defaultQueuName;
    }
}
