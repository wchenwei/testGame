package com.hm.libcore.inner.client;

import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.serverConfig.ServerConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.ByteString;

import cn.hutool.core.util.StrUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "inner")
public class BaseInnerClient {
	private Bootstrap bootstrap;
    private Channel channel;
    
    private String host;
    private int port;
    private String clientIp;
    private AbstractInnerClientHandler requestHandler;
    private InnerClientTimer clientTimer;
    
	public void connectServer() {
        try {
        	if(this.clientTimer == null) {
    			this.clientTimer = new InnerClientTimer(this);
    			this.clientTimer.startCheckServer();
    		}
    		close();
    		System.setProperty("java.net.preferIPv4Stack", "true");  
            System.setProperty("java.net.preferIPv6Addresses", "false");  
        	EventLoopGroup group = new NioEventLoopGroup();
    		bootstrap = new Bootstrap();
    		bootstrap.group(group).channel(NioSocketChannel.class);
    		bootstrap.handler(new ChildChannelHandler(requestHandler));
    		
    		this.channel = bootstrap.connect(host, port).sync().channel();
    		log.error("连接内网服务器"+host+"成功");
    		checkClient();
		} catch (Exception e) {
			log.error("内网客户端连接异常："+host+":"+port, e);
		}
	}
	
	protected class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
		private AbstractInnerClientHandler requestHandler;
		public ChildChannelHandler(AbstractInnerClientHandler requestHandler) {
			super();
			this.requestHandler = requestHandler;
		}
		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
			channel.pipeline().addLast(new ProtobufDecoder(HMProtobuf.HMRequest.getDefaultInstance()));
			channel.pipeline().addLast(new LengthFieldPrepender(4));
			channel.pipeline().addLast(new ProtobufEncoder());
			channel.pipeline().addLast(new InnerClientHandler(requestHandler));
		}
	}
	
	public void sendMsg(Object obj) {
		if(channel != null) {
			channel.writeAndFlush(obj);
		}
	}
	
	public boolean isConnect() {
		try {
			return this.channel != null && this.channel.isActive();
		} catch (Exception e) {
			log.error("内网客户端连接异常：isConnect()"+host+":"+port, e);
		}
		return false;
	}
	
	public void checkClient() {
		sendMsg(createRequest(-1, getMachineId()));
	}

	public void close() {
		try {
			this.bootstrap = null;
			if(this.channel != null) {
				this.channel.close();
			}
			this.channel = null;
		} catch (Exception e) {
			log.error("内网客户端连接异常：close()"+host+":"+port, e);
		}
	}
	
	public int getMachineId() {
		return ServerConfig.getInstance().getMachineId();
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getClientIp() {
		return clientIp;
	}

	public void setRequestHandler(AbstractInnerClientHandler requestHandler) {
		this.requestHandler = requestHandler;
	}
	
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
	public boolean isErrorClient() {
		return StrUtil.isEmpty(this.host) || this.port == 0;
	}

	public  HMProtobuf.HMRequest createRequest(int msgId,Object obj) {
		HMProtobuf.HMRequest.Builder builder = HMProtobuf.HMRequest.newBuilder();
		builder.setMsgId(msgId);
		builder.setData(ByteString.copyFromUtf8(JSONUtil.toJson(obj)));
		return builder.build();
	}
}
