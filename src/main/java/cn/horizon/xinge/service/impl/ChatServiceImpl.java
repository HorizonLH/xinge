package cn.horizon.xinge.service.impl;

import cn.horizon.xinge.common.netty.ChatErrorEnum;
import cn.horizon.xinge.common.netty.ReturnMessage;
import cn.horizon.xinge.common.netty.UserChannelRel;
import cn.horizon.xinge.netty.handler.WebSocketServerHandler;
import cn.horizon.xinge.netty.object.ChatContent;
import cn.horizon.xinge.netty.object.ChatObject;
import cn.horizon.xinge.service.ChatService;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

/**
 * @author horizon
 * @create 2023/3/29 20:46
 **/
@Service
public class ChatServiceImpl implements ChatService {
    @Override
    public void receiveMsg(ChatContent content) {
        ChatContent receiverContent = new ChatContent();
        ChatObject senderData = content.getData();
        Channel receiverChannel = UserChannelRel.get(senderData.getReceiverId().toString());
        Channel senderChannel = UserChannelRel.get(senderData.getSenderId().toString());
        if (senderChannel == null) {
            System.out.println(senderData.getSenderId() + "未连接");
            return;
        }
        receiverContent.setData(senderData);

        if (receiverChannel == null) {
            //用户不在线
            ReturnMessage.returnErrorMsg(senderChannel, ChatErrorEnum.USER_NOT_CONNECT, senderData.getReceiverId().toString());
        } else {
            Channel findChannel = WebSocketServerHandler.users.find(receiverChannel.id());
            if (findChannel != null) {
                ReturnMessage.returnMsg(receiverChannel, receiverContent);
            }
        }

    }
}
