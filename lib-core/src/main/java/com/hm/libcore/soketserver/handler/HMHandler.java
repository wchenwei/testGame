/**
 * 
 */
package com.hm.libcore.soketserver.handler;

import com.hm.libcore.handler.RequestHandler;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.serverConfig.ServerConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Title: BaseHandler.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年10月30日 下午2:41:49
 * @version 1.0
 */
@Slf4j
public class HMHandler extends SimpleChannelInboundHandler<HMRequest> {

	private RequestHandler requestHandler;
	
	public HMHandler() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.requestHandler = (RequestHandler)Class.forName(ServerConfig.getInstance().getRequestHandler()).newInstance();
	}

	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		ctx.close();
		HMSession session = new HMSession(ctx);
		ServerSessions.getInstance().put(session.id(), session);
		log.info("socket链接建立:" + ctx.name() + ctx.channel().remoteAddress());
		this.requestHandler.sessionCreated(session);
	}
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HMRequest param) throws Exception {
		HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
//		debugRecv(param);
		this.requestHandler.messageReceived(session, param);
	}


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
//    	throwable.printStackTrace();
    	try {
    		log.info(ctx.name() + ctx.channel().remoteAddress() + "异常关闭连接");
    		HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
    		this.requestHandler.exceptionCaught(session);
    		session.close();
		} catch (Exception e) {
			ctx.close();
		}
    }

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    	try {
    		log.info(ctx.name() + ctx.channel().remoteAddress() + "关闭连接");
        	HMSession session = ServerSessions.getInstance().remove(ctx.channel().id().asShortText());
    		if(session != null) {
    			this.requestHandler.sessionClosed(session);
    			session.close();
    		}
		} catch (Exception e) {
			ctx.close();
		}
	}

	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
		/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                /*读超时*/
//                log.info("READER_IDLE 读超时");
//                ctx.disconnect();
//            } else if (event.state() == IdleState.WRITER_IDLE) {
//                /*写超时*/   
//            	log.info("WRITER_IDLE 写超时");
//            } else 
            if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
               HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
     		   log.info(ctx.name() + ctx.channel().remoteAddress() + "掉线"); 
     		   this.requestHandler.sessionIdel(session);
     		   session.close();
            }
        }
	}
	
	
	private void debugRecv(HMRequest msg){
		if(ServerConfig.getInstance().isDebug()){
			Map<String,Object> req = new HashMap<String,Object>();
			req.put("action", msg.getMsgId());
			req.put("data", msg.getData().toStringUtf8());
			
			log.info("recv://   " + JSONUtil.toJson(req));
		}
	}



	
}
