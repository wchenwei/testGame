package com.hm.libcore.websocket.server;

import com.hm.libcore.enums.SocketType;
import com.hm.libcore.handler.RequestHandler;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.soketserver.handler.ServerSessions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{
    /**
     * 全局websocket
     */
    private WebSocketServerHandshaker handshaker;
    
    private RequestHandler requestHandler;
    
    public WebSocketServerHandler() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.requestHandler = (RequestHandler)Class.forName(ServerConfig.getInstance().getRequestHandler()).newInstance();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	HMSession session = new HMSession(ctx);
    	session.setSocketType(SocketType.H5.getType());
		ServerSessions.getInstance().put(session.id(), session);
		log.info(session.id()+":socket链接建立:" + ctx.name() + ctx.channel().remoteAddress());
		this.requestHandler.sessionCreated(session);
	}
    
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest){ //如果是HTTP请求，进行HTTP操作
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if(msg instanceof WebSocketFrame){ //如果是Websocket请求，则进行websocket操作
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        log.info(ctx.name() + ctx.channel().remoteAddress() + "异常关闭连接");
    	HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
    	this.requestHandler.exceptionCaught(session);
    	session.close();
    }
    
    @Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		log.info(ctx.channel().id().asShortText()+":" + ctx.name() + ctx.channel().remoteAddress() + "正常退出");
		super.handlerRemoved(ctx);
		HMSession session = ServerSessions.getInstance().remove(ctx.channel().id().asShortText());
		if(session != null) {
			this.requestHandler.sessionClosed(session);
			session.close();
		}
	}
    
    @Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
		/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
               HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
     		   log.info(ctx.name() + ctx.channel().remoteAddress() + "掉线"); 
     		   this.requestHandler.sessionIdel(session);
     		   session.close();
            }
        }
	}
    
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request){
        //如果http解码失败 则返回http异常 并且判断消息头有没有包含Upgrade字段(协议升级)
        if(!request.getDecoderResult().isSuccess() 
                || (!"websocket".equals( request.headers().get("Upgrade")))    ){
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return ;
        }
        //构造握手响应返回
        WebSocketServerHandshakerFactory ws = new WebSocketServerHandshakerFactory("", null, false);
        this.handshaker = ws.newHandshaker(request);
        if(this.handshaker == null){
            //版本不支持
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }else{
        	this.handshaker.handshake(ctx.channel(), request);
        }
    }
    /**
     * websocket帧
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        //判断是否关闭链路指令
        if(frame instanceof CloseWebSocketFrame){
        	this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return ;
        }
        //判断是否Ping消息 -- ping/pong心跳包
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return ;
        }
        if(frame instanceof BinaryWebSocketFrame) {
        	BinaryWebSocketFrame request = (BinaryWebSocketFrame)frame;
        	ByteBuf byteBuf = request.content();
        	byte[] req = new byte[byteBuf.readableBytes()];
        	byteBuf.readBytes(req);
        	HMRequest param = null;
        	try {
        		param = HMRequest.parseFrom(req);
			} catch (Exception e) {
				return;
			}
        	HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
    		this.requestHandler.messageReceived(session, param);
        	return;
        }
//        //本程序仅支持文本消息， 不支持二进制消息
//        if(!(frame instanceof TextWebSocketFrame)){
//            throw new UnsupportedOperationException(
//                    String.format("%s frame types not supported", frame.getClass().getName()));
//        }
//        HMSession session = ServerSessions.getInstance().get(ctx.channel().id().asShortText());
//        session.sendErrorMsg(100, 1);
//        
//        //返回应答消息 text文本帧
//        String request = ((TextWebSocketFrame) frame).text();
//        //发送到客户端websocket
//        ctx.channel().write(new TextWebSocketFrame(request 
//                + ", 欢迎使用Netty WebSocket服务， 现在时刻:" 
//                + new java.util.Date().toString()));
        
    }
    
    /**
     * response
     * @param ctx
     * @param request
     * @param response
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, 
            FullHttpRequest request, FullHttpResponse response){
        //返回给客户端
        if(response.getStatus().code() != HttpResponseStatus.OK.code()){
            ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
//            HttpHeaderUtil.setContentLength(response, response.content().readableBytes());
            
        }
        //如果不是keepalive那么就关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if(
//        		!HttpHeaderUtil.isKeepAlive(response) ||
                 response.getStatus().code() != HttpResponseStatus.OK.code()){
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    
}