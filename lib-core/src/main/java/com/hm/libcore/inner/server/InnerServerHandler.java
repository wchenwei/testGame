/**
 * 
 */
package com.hm.libcore.inner.server;

import com.hm.libcore.inner.InnerSessions;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.protobuf.HMProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InnerServerHandler extends SimpleChannelInboundHandler<HMProtobuf.HMRequest> {
	private AbstractInnerServerHandler requestHandler;
	
	public InnerServerHandler(AbstractInnerServerHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		ctx.close();
		HMSession session = new HMSession(ctx);
		InnerSessions.getInstance().put(session.id(), session);
		log.info("socket链接建立:" + ctx.name() + ctx.channel().remoteAddress());
		this.requestHandler.sessionCreated(session);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HMProtobuf.HMRequest param) throws Exception {
		HMSession session = InnerSessions.getInstance().get(ctx.channel().id().asShortText());
		if(param.getMsgId() == -1) {
			this.requestHandler.registClient(session, param);
			return;
		}
		this.requestHandler.messageReceived(session, param);
		
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
    	throwable.printStackTrace();
    	log.info(ctx.name() + ctx.channel().remoteAddress() + "异常关闭连接");
    	HMSession session = InnerSessions.getInstance().get(ctx.channel().id().asShortText());
    	this.requestHandler.exceptionCaught(session);
    	session.close();
    }
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		log.info("handlerRemoved" + ctx.name() + ctx.channel().remoteAddress() + "正常退出");
		super.handlerRemoved(ctx);
		HMSession session = InnerSessions.getInstance().remove(ctx.channel().id().asShortText());
		this.requestHandler.sessionClosed(session);
		session.close();
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
               HMSession session = InnerSessions.getInstance().get(ctx.channel().id().asShortText());
     		   log.info(ctx.name() + ctx.channel().remoteAddress() + "掉线"); 
     		   this.requestHandler.sessionIdel(session);
     		   session.close();
            }
        }
	}
	
}
