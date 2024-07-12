package com.hm.libcore.util.httppool;

public class ClientConfig {
    // 默认的获取连接的超时时间, 单位ms
    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 10 * 1000;
    // 默认连接超时, 单位ms
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
    // 默认的SOCKET读取超时时间, 单位ms
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    // 默认的维护最大HTTP连接数
    private static final int DEFAULT_MAX_CONNECTIONS_COUNT = 512;
    // 多次签名的默认过期时间,单位秒
    private static final long DEFAULT_SIGN_EXPIRED = 3600;
    // Read Limit
    private static final int DEFAULT_READ_LIMIT = (2 << 17) + 1;

    private long signExpired = DEFAULT_SIGN_EXPIRED;
    private int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    private int maxConnectionsCount = DEFAULT_MAX_CONNECTIONS_COUNT;
    private int readLimit = DEFAULT_READ_LIMIT;

    // 不传入region 用于后续调用List Buckets(获取所有的bucket信息)
    public ClientConfig() {
        super();
    }

	public long getSignExpired() {
		return signExpired;
	}

	public void setSignExpired(long signExpired) {
		this.signExpired = signExpired;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getMaxConnectionsCount() {
		return maxConnectionsCount;
	}

	public void setMaxConnectionsCount(int maxConnectionsCount) {
		this.maxConnectionsCount = maxConnectionsCount;
	}

	public int getReadLimit() {
		return readLimit;
	}

	public void setReadLimit(int readLimit) {
		this.readLimit = readLimit;
	}
    
    
}
