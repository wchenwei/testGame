package com.hm.libcore.websocket.server;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

public class WebsocketEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder> {
    @Override
    protected void encode(
            ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
        if (msg instanceof MessageLite) {
            out.add(createData(((MessageLite) msg).toByteArray()));
            return;
        }
        if (msg instanceof MessageLite.Builder) {
            out.add(createData(((MessageLite.Builder) msg).build().toByteArray()));
        }
    }
    
    private BinaryWebSocketFrame createData(byte[] data) {
    	return new BinaryWebSocketFrame(Unpooled.wrappedBuffer(data));
    }
}
