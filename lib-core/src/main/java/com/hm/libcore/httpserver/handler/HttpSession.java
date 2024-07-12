package com.hm.libcore.httpserver.handler;
/**
 * Title: HttpSession.java
 * Description:HTTPSession封装
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014-7-31
 * @version 1.0
 */

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.string.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


@Slf4j
public class HttpSession {
	/**
	 * 
	 */
	private ChannelHandlerContext _channel = null;
	
	private FullHttpRequest _request = null;

	private FullHttpResponse _response = null;
	
	private String _url = null;
	
	private String _content = null;
	
	private Map<String, String> _params = null;
	
	private String _path = null;
	
	private boolean _closing = false;

	/**
	 * 
	 */
	public HttpSession() {
		
	}

	/**
	 * 
	 * @param channel
	 */
	public HttpSession(ChannelHandlerContext channel, FullHttpRequest request) {
		try {
			this._channel = channel;
			this._request = request;
			this._response = new DefaultFullHttpResponse(HTTP_1_1, OK);
			this._response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
			this._response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			this._url = this._request.getUri();
//			this._content = this._request.content().toString(CharsetUtil.UTF_8);
			this._content = URLDecoder.decode(this._request.content().toString(CharsetUtil.UTF_8), "utf-8");
			StringBuilder sb = new StringBuilder();
			this._params = StringUtil.Url2Map(this._url, sb);
			this._path = sb.toString();
			this._params = getParamsMap();
		} catch (Exception e) {
		}
	}
	
	
	/**
	 * 按请求方式解析组装http传入的参数
	 * @param session
	 * @return
	 */
	private Map<String,String> getParamsMap(){
		//判断get请求或者是POST ,获取参数方法不一样
		Map<String,String> map = null;
		Map<String,String> getMap = null;
		if( this._request.getMethod() == HttpMethod.POST ) {
			//这里是POST
			getMap = this.getParams();
			map = StringUtil.Url2Map(this._content);
			map.putAll(getMap);
		}
		else if ( this._request.getMethod() == HttpMethod.GET ) {
			//这里是GET
			map = this.getParams();
		}
		return map;
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void setHeader(String arg0, Object arg1) {
		this._response.headers().set(arg0, arg1);
	}


	/**
	 * 
	 * @param content
	 */
	public void Write(String content) {
		ByteBuf buffer = Unpooled.copiedBuffer(content,
				CharsetUtil.UTF_8);
		try{
			this._response.content().writeBytes(buffer);
			this._response.headers().set(CONTENT_LENGTH, buffer.writerIndex());
	//		log.info("buffer.writerIndex:" +  buffer.writerIndex());
	//		log.info("buffer.readableBytes:" +  buffer.readableBytes());
	//		log.info("buffer.writableBytes:" +  buffer.writableBytes());
			this._channel.writeAndFlush(this._response).addListener(
					ChannelFutureListener.CLOSE);
			this._closing = true;
			if(ServerConfig.getInstance().isDebug()){
				log.info("Http Send:" + content);
			}
		}finally{
			buffer.release();
		}
	}
	
	/**
	 * 
	 */
	public void NOT_FOUND(String content) {
		FullHttpResponse _not_found = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
		ByteBuf buffer = Unpooled.copiedBuffer(content,
				CharsetUtil.UTF_8);
		_not_found.content().writeBytes(buffer);
		_not_found.headers().set(CONTENT_LENGTH, buffer.writerIndex());
		this._channel.writeAndFlush(_not_found).addListener(
				ChannelFutureListener.CLOSE);
		this._closing = true;
	}
	
	/**
	 * 
	 * @param content
	 */
	public void Write(byte[] content) {
		ByteBuf buffer = Unpooled.copiedBuffer(content);
		try{
			this._response.content().writeBytes(buffer);
			this._response.headers().set(CONTENT_TYPE, buffer.writerIndex());
	//		log.info("buffer.writerIndex:" +  buffer.writerIndex());
	//		log.info("buffer.readableBytes:" +  buffer.readableBytes());
	//		log.info("buffer.writableBytes:" +  buffer.writableBytes());
			this._channel.writeAndFlush(this._response).addListener(
					ChannelFutureListener.CLOSE);
			this._closing = true;
			if(ServerConfig.getInstance().isDebug()){
				log.info("Http Send:" + new String(content));
			}
		}finally{
			buffer.release();
		}
	}
	
	/**
	 * 
	 */
	public void Close() {
		this._channel.close();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean Closing() {
		return this._closing;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		return this._url;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getParams() {
		return this._params;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getParams(String key) {
		return this._params.get(key);
	}
 	
	/**
	 * 
	 * @return
	 */
	public String getContent() {
		return this._content;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPath() {
		return this._path;
	}
	
	/**
	 * 
	 * @return
	 */
	public HttpRequest getRequest() {
		return this._request;
	}
	
	/**
	 * 
	 * @return
	 */
	public HttpResponse getResponse() {
		return this._response;
	}
	
	
	public String getRemoteAddress(){
		String ip = null;
		if(this._request.headers().contains("X-Real-IP")){
			ip = this._request.headers().get("X-Real-IP");
		}
		String remoteAddress = this._channel.channel().remoteAddress().toString().replace("/", "");
		String[] address = remoteAddress.split(":");
		if(ip == null){
			ip = address[0];
		}
		return ip + ":" + address[1];
	}
	
	public String getRemoteIp(){
		String ip = null;
		if(this._request.headers().contains("X-Real-IP")){
			ip = this._request.headers().get("X-Real-IP");
		}
		if(ip == null){
			String remoteAddress = this.getRemoteAddress().replace("/", "");
			String[] address = remoteAddress.split(":");
			ip = address[0]; 
		}
		return ip;
	}
	
}
