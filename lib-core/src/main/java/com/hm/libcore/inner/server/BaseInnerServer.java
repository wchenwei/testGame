/**
 * 
 */
package com.hm.libcore.inner.server;

import com.hm.libcore.protobuf.HMProtobuf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseInnerServer {

	private int port;
	private AbstractInnerServerHandler requestHandler;
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;
	
	public BaseInnerServer() {
		super();
	}

	public void start(){
		int port = getPort();
		this.bossGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup(); 
		try{
			this.serverBootstrap = new ServerBootstrap();
			this.serverBootstrap.group(bossGroup, workerGroup);
			this.serverBootstrap.channel(NioServerSocketChannel.class);
			this.serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
			this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			this.serverBootstrap.handler(new LoggingHandler());
			this.serverBootstrap.childHandler(new ChildChannelHandler(requestHandler));
			
			//绑定端口，同步等待成功
			ChannelFuture f = this.serverBootstrap.bind(port).sync();
			System.err.println(this.getClass().getSimpleName()+" start success at "+port+"..........");
			//等待服务器监听端口关闭
			f.channel().closeFuture().addListener( new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					future.channel().close();
					shutdown();
				}
		    });
		} catch (InterruptedException e) {
			log.error(this.getClass().getSimpleName()+" start failed at "+port+"..........", e);
		}
//		}finally{//释放线程池资源，退出
//			this.bossGroup.shutdownGracefully();
//			this.workerGroup.shutdownGracefully();
//		}
		
	}
	
	protected class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
		private AbstractInnerServerHandler requestHandler;
		
		public ChildChannelHandler(AbstractInnerServerHandler requestHandler) {
			super();
			this.requestHandler = requestHandler;
		}

		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
			channel.pipeline().addLast(new ProtobufDecoder(HMProtobuf.HMRequest.getDefaultInstance()));
			channel.pipeline().addLast(new LengthFieldPrepender(4));
			channel.pipeline().addLast(new ProtobufEncoder());
			channel.pipeline().addLast(new InnerServerHandler(this.requestHandler));
		}
	}
	
	private void shutdown(){
		this.bossGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setRequestHandler(AbstractInnerServerHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	public int getPort() {
		return port;
	}
	
}
