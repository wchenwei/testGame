/**
 * 
 */
package com.hm.libcore.soketserver.server;

import cn.hutool.core.thread.NamedThreadFactory;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.soketserver.NettyUtil;
import com.hm.libcore.soketserver.handler.HMHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Title: BaseServer.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年10月30日 下午2:14:44
 * @version 1.0
 */
@Slf4j
public abstract class BaseServer {

	public BaseServer() {
		super();
	}


	/** boss event loop group, boss group should not be daemon, need shutdown manually*/
	private final EventLoopGroup                        bossGroup               = NettyUtil
			.newEventLoopGroup(
					1,
					new NamedThreadFactory(
							"game-netty-server-boss",
							false));
	/** worker event loop group. Reuse I/O worker threads between rpc servers. */
	private static final EventLoopGroup                 workerGroup             = NettyUtil
			.newEventLoopGroup(
					Runtime
							.getRuntime()
							.availableProcessors() * 2,
					new NamedThreadFactory(
							"game-netty-server-worker",
							true));


	private ServerBootstrap serverBootstrap;
	public void start(){

		try{
			this.serverBootstrap = new ServerBootstrap();
			this.serverBootstrap.group(bossGroup, workerGroup)
					.channel(NettyUtil.getServerSocketChannelClass())
					// 此hander是所有客户端连接都会经过的hander，就是只有这一个hander 单例
					.handler(new LoggingHandler(LogLevel.WARN))
					// 此hander是所有客户端都有一个hander,工厂模式创建出hander
					.childHandler(new ChildChannelHandler())
					.option(ChannelOption.SO_BACKLOG, 1024) // 输入连接指示（对连接的请求）的最大队列长度。如果队列满时收到连接指示，则拒绝该连接。FIFO（先进先出）的原则
					.childOption(ChannelOption.SO_KEEPALIVE, true)//开启时系统会在连接空闲一定时间后像客户端发送请求确认连接是否有效
			        .childOption(ChannelOption.TCP_NODELAY, true)//关闭Nagle算法 NAGLE算法通过将缓冲区内的小封包自动相连，组成较大的封包，阻止大量小封包的发送阻塞网络，从而提高网络应用效率。但是对于时延敏感的应用场景需要关闭该优化算法；
					.option(ChannelOption.SO_KEEPALIVE, true)

//					.childOption(ChannelOption.SO_SNDBUF, 4096)//系统sockets发送数据buff的大小
//					.childOption(ChannelOption.SO_RCVBUF, 2048)//---接收
//					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//使用bytebuf池, 默认不使用
//					.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)//使用bytebuf池, 默认不使用
					;

			System.out.println("game netty启动:"+(NettyUtil.epollEnabled?"Linux":"Windows ") + getPort());
			this.serverBootstrap.bind(new InetSocketAddress(getPort()));
			
		} catch (Exception e) {
			log.error(this.getClass().getSimpleName()+" start failed at "+getPort()+"..........", e);
		}
//		}finally{//释放线程池资源，退出
//			this.bossGroup.shutdownGracefully();
//			this.workerGroup.shutdownGracefully();
//		}
		
	}
	
	protected class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
//			channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
//			channel.pipeline().addLast(new ProtobufDecoder(HMProtobuf.HMRequest.getDefaultInstance()));
//			channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
//			channel.pipeline().addLast(new LengthFieldPrepender(4));
//			channel.pipeline().addLast(new ProtobufEncoder());
//			channel.pipeline().addLast(new IdleStateHandler(0, 0, getIdleSec()));
			
			channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
			channel.pipeline().addLast(new ProtobufDecoder(HMProtobuf.HMRequest.getDefaultInstance()));
			channel.pipeline().addLast(new LengthFieldPrepender(4));
			channel.pipeline().addLast(new ProtobufEncoder());
			channel.pipeline().addLast(new IdleStateHandler(getReadTimeOut(), getWriteTimeOut(), getIdleSec()));
			channel.pipeline().addLast(new HMHandler());
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
}
