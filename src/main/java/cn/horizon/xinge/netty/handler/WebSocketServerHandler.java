package cn.horizon.xinge.netty.handler;

import cn.horizon.xinge.common.api.Result;
import cn.horizon.xinge.common.netty.ChatActionEnum;
import cn.horizon.xinge.common.netty.ChatErrorEnum;
import cn.horizon.xinge.common.netty.ReturnMessage;
import cn.horizon.xinge.common.netty.UserChannelRel;
import cn.horizon.xinge.netty.object.ChatContent;
import cn.horizon.xinge.service.ChatService;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Netty 服务端Handler，处理客户端发送的消息
 * @author horizon
 * @create 2022-06-21 21:03
 **/
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);

    //定义channel集合,管理channel,传入全局事件执行器
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    private ChatService chatService;

    /**
     * 读取完连接的消息后，对消息进行处理。
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
        handleWebSocketFrame(ctx, msg);
    }

    /**
     * 处理WebSocketFrame
     * @param ctx Channel上下文对象
     * @param msg 客户端消息
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        //关闭请求
        if (msg instanceof CloseWebSocketFrame) {
            ctx.channel().close();
        }

        //ping请求,将原内容返回
        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
            return;
        }

        if (!(msg instanceof TextWebSocketFrame)) {
            ReturnMessage.returnErrorMsg(ctx.channel(), ChatErrorEnum.SUPPORT_TEXT_ONLY);
            return;
        }

        // 客服端发送过来的消息
        String message = ((TextWebSocketFrame)msg).text();
        LOGGER.info("服务端收到新信息：" + message);
        ChatContent content = JSON.parseObject(message, ChatContent.class);
//
        if (content == null || content.getAction() == null) {
            ReturnMessage.returnErrorMsg(ctx.channel(), ChatErrorEnum.MSG_ERROR);
            return;
        }
        ChatActionEnum action = content.getAction();
        Channel currentChannel = ctx.channel();
        switch (action) {
            case CONNECT -> UserChannelRel.put(content.getData().getSenderId().toString(), currentChannel);
            case CHAT -> chatService.receiveMsg(content);
            case SIGNED -> chatService.signMsg(content);
        }
    }

    /**
     * 当客户端连接服务端之后(打开连接)----->handlerAdded
     * 获取客户端的channel,并且放到ChannelGroup中去管理
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        users.add(ctx.channel());
    }

    //处理器移除时,移除channelGroup中的channel
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        //打印移除的channel
        String asShortText = ctx.channel().id().asShortText();
        LOGGER.info("客户端被移除，channelId为：{}", asShortText);
        users.remove(ctx.channel());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //TODO 断开连接
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        users.remove(ctx.channel());
        ctx.close();
        LOGGER.info("【" + ctx.channel().remoteAddress() + "】已关闭（服务器端）");
    }
}
