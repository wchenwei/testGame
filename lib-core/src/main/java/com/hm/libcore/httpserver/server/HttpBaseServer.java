package com.hm.libcore.httpserver.server;

import com.hm.libcore.httpserver.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Title: HttpServer.java
 * Description:HttpServer类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014-7-31
 * @version 1.0
 */
@Slf4j
public abstract class HttpBaseServer {

	private EventLoopGroup boss;
	private EventLoopGroup worker;
	private ServerBootstrap serverBootstrap;
	public void start() throws InterruptedException{
		
		boss = new NioEventLoopGroup();
		worker = new NioEventLoopGroup(getHttpMaxThreadCount());
		try{
			this.serverBootstrap = new ServerBootstrap();
			this.serverBootstrap.group(boss, worker);
			this.serverBootstrap.channel(NioServerSocketChannel.class);
			this.serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
			this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			this.serverBootstrap.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
//					ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
					ch.pipeline().addLast("http-servercodec",new HttpServerCodec());
					ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(1024*1024*2));
//					ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
					ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
					ch.pipeline().addLast("http-deflate",new HttpContentCompressor());
//					ch.pipeline().addLast(new IdleStateHandler(getHttpReadIdel(), getHttpWriteIdel(), getHttpIdel()));
					ch.pipeline().addLast("handler",new HttpServerHandler());
					
				}
			});
			ChannelFuture f = this.serverBootstrap.bind(getHttpPort()).sync();

			log.info(this.getClass().getSimpleName()+" start success at "+ getHttpPort() +"..........");
			
			/*f.channel().closeFuture().addListener( new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					future.channel().close();
					shutdown();
				}
		    }).sync();*/
		} catch (InterruptedException e) {
			log.error(this.getClass().getSimpleName()+" start failed at "+ getHttpPort() +"..........", e);
		}
//		finally{
//			this.boss.shutdownGracefully();
//			this.worker.shutdownGracefully();
//		}
		
		
	}
	
	
	private void shutdown(){
		this.boss.shutdownGracefully();
		this.worker.shutdownGracefully();
	}
	
	public abstract int getHttpPort();
	
	public abstract int getHttpMaxThreadCount();
	
	public abstract int getHttpIdel();
	
	public abstract int getHttpWriteIdel();
	
	public abstract int getHttpReadIdel();
	
}

