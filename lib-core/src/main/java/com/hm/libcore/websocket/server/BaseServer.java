package com.hm.libcore.websocket.server;

import cn.hutool.core.thread.NamedThreadFactory;
import com.hm.libcore.serverConfig.H5Config;
import com.hm.libcore.soketserver.NettyUtil;
import com.hm.libcore.util.SslUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

@Slf4j
public abstract class BaseServer {
	public BaseServer() {
		super();
	}

	/** boss event loop group, boss group should not be daemon, needNettyUtil shutdown manually*/
	private final EventLoopGroup                        bossGroup               = NettyUtil
			.newEventLoopGroup(
					1,
					new NamedThreadFactory(
							"h5-netty-server-boss",
							false));
	/** worker event loop group. Reuse I/O worker threads between rpc servers. */
	private static final EventLoopGroup                 workerGroup             = NettyUtil
			.newEventLoopGroup(
					Runtime
							.getRuntime()
							.availableProcessors() * 2,
					new NamedThreadFactory(
							"h5-netty-server-worker",
							true));

	private ServerBootstrap serverBootstrap;
	
	public void start(){
        try {
        	this.serverBootstrap = new ServerBootstrap();
        	this.serverBootstrap.group(bossGroup, workerGroup);
        	this.serverBootstrap.channel(NettyUtil.getServerSocketChannelClass());
        	this.serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
			this.serverBootstrap.option(ChannelOption.SO_TIMEOUT, 10);
			this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			this.serverBootstrap.childHandler(new ChildChannelHandler());
        	
        	//绑定端口，同步等待成功
			ChannelFuture f = this.serverBootstrap.bind(getPort()).sync();
			log.info(this.getClass().getSimpleName()+" start success at "+getPort()+"..........");
			//等待服务器监听端口关闭
			f.channel().closeFuture().addListener( new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					future.channel().close();
					shutdown();
				}
		    }).sync();
			System.out.println("h5 netty启动:"+(NettyUtil.epollEnabled?"Linux":"Windows"));
			System.err.println("======h5端口启动===="+getPort());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}
	
	protected class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			if(H5Config.getInstance().isSslServer()) {
				log.info(this.getClass().getSimpleName()+" sslContext 初始化 at ["+getSSLKeyPath()+"]["+getSSLKeyPwd()+"]");
				SSLContext sslContext = SslUtils.createSSLContext("JKS",getSSLKeyPath(),getSSLKeyPwd());
				//SSLEngine 此类允许使用ssl安全套接层协议进行安全通信
				SSLEngine engine = sslContext.createSSLEngine(); 
				engine.setUseClientMode(false); 
				channel.pipeline().addLast(new SslHandler(engine));
			}
			
			channel.pipeline().addLast(new HttpServerCodec());
			channel.pipeline().addLast(new HttpObjectAggregator(65536));
			channel.pipeline().addLast(new IdleStateHandler(getReadTimeOut(), getWriteTimeOut(), getIdleSec()));
			channel.pipeline().addLast(new WebsocketEncoder());
			channel.pipeline().addLast(new WebSocketServerHandler());
		}
		
	}
	
	private void shutdown(){
		this.bossGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
	}
	
	
	public abstract int getPort();
	
	public abstract int getThreadCount();
	
	public abstract int getIdleSec();
	
	public abstract int getWriteTimeOut();
	
	public abstract int getReadTimeOut();
	
	public abstract String getSSLKeyPath();
	
	public abstract String getSSLKeyPwd();
}
