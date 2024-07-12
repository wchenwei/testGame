package com.hm.libcore.httpserver.handler;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.gson.GSONUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Title: HttpServerHandler.java
 * Description:HttpServerHandler
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014-7-31
 * @version 1.0
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		try{
			HttpSession session = new HttpSession(ctx,request);
			if(ServerConfig.getInstance().isDebug()){
				log.info(session.getRemoteAddress() + "   Http Recv:" + session.getParams());
			}
			if(!session.getParams().containsKey("action") || !session.getParams().containsKey("m")){
				System.err.println(GSONUtils.ToJSONString(session.getParams()));
				return;
			}
			Object obj = SpringUtil.getBean(session.getParams("action"));
			String methodName = session.getParams("m");
			if(obj == null){
				log.error(session.getParams("action") + "==action不存在");
				return;
			}
			Method[] methods = obj.getClass().getMethods();
			boolean haveMethod = false;
			for(Method method : methods){
				if(method.getName().equals(methodName)){
					haveMethod = true;
				}
			}
			if(haveMethod){
				try {
					obj.getClass().getMethod(methodName, HttpSession.class).invoke(obj, session);
				} catch (Exception e) {
					log.error("http请求处理出错:"+GSONUtils.ToJSONString(session.getParams()));
				}
			}else{
				log.error("方法：" + methodName + "不存在");
			}
		}catch (Exception e) {
			log.error("http请求处理出错", e);
		}finally{
			ctx.close();
		}
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
			log.error("捕捉到HttpServer业务层之外的异常", cause);
			ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
		/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                /*读超时*/
                log.info("READER_IDLE 读超时");
                log.info("http链接" + ctx.name() + ctx.channel().remoteAddress() + "空闲断开"); 
      		    ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*写超时*/   
            	log.info("WRITER_IDLE 写超时");
            	log.info("http链接" + ctx.name() + ctx.channel().remoteAddress() + "空闲断开"); 
      		    ctx.close();
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
            	log.info("ALL_IDLE 读写超时");
     		   log.info("http链接" + ctx.name() + ctx.channel().remoteAddress() + "空闲断开"); 
     		   ctx.close();
            }
        }
	}

	
	
}
