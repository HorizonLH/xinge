package cn.horizon.xinge.common.netty;

import cn.horizon.xinge.common.api.Result;
import cn.horizon.xinge.netty.object.ChatContent;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.MessageFormat;

/**
 * @author horizon
 * @create 2023/3/29 21:07
 **/
public class ReturnMessage {

    public static void returnMsg(Channel channel, ChatContent content) {
        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(content)));
    }

    public static void returnErrorMsg(Channel channel, ChatErrorEnum error, Object ...arguments) {
        String formatMessage = MessageFormat.format(error.getMsg(), arguments);
        String responseJson = JSON.toJSONString(Result.failed(error.getCode(), formatMessage));
        channel.writeAndFlush(new TextWebSocketFrame(responseJson));
    }

}
