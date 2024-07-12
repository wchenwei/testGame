package com.hm.libcore.rmi;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

/**
 * 自定一个SCOKECT连接，可配置超时时间
 * @author Henry
 */
@Slf4j
public class RMICustomClientSocketFactory implements RMIClientSocketFactory {

    private int timeout;
    
    /**
     * 设置超时时间
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Socket createSocket(String host, int port) 
    {
    	
        Socket socket = null;
		try {
			socket = new Socket(host, port);
			socket.setSoTimeout(timeout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("RMI连接建立失败");
		}
        return socket;
    }

}
