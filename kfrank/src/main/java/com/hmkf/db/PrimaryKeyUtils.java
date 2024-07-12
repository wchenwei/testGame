package com.hmkf.db;

public class PrimaryKeyUtils {
    public static int getIncrementKey(String keyName) {
        return KfDBUtils.getMongoDB().getIncrementKey(keyName, 1);
    }

    public static String getStrIncrementKey(String keyName) {
        return Integer.toString(getIncrementKey(keyName), 36);
    }
}
