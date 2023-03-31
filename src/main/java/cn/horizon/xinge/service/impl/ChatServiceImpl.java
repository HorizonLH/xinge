package cn.horizon.xinge.service.impl;

import cn.horizon.xinge.common.netty.ChatErrorEnum;
import cn.horizon.xinge.common.netty.ReturnMessage;
import cn.horizon.xinge.common.netty.UserChannelRel;
import cn.horizon.xinge.domain.ChatMsg;
import cn.horizon.xinge.netty.handler.WebSocketServerHandler;
import cn.horizon.xinge.netty.object.ChatContent;
import cn.horizon.xinge.netty.object.ChatObject;
import cn.horizon.xinge.repository.ChatDao;
import cn.horizon.xinge.service.ChatService;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author horizon
 * @create 2023/3/29 20:46
 **/
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatDao chatDao;

    @Transactional
    @Override
    public void receiveMsg(ChatContent content) {
        ChatContent receiverContent = new ChatContent();
        ChatObject senderData = content.getData();

        // 保存消息
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setSendUid(senderData.getSenderId());
        chatMsg.setAcceptUid(senderData.getReceiverId());
        chatMsg.setSignFlag(false);
        chatMsg.setMsg(senderData.getMsg());

        chatDao.insert(chatMsg);

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

    @Override
    public void signMsg(ChatContent content) {
        //使用extend字段，获取到用逗号分隔的需要签收的消息id
        String extend = content.getExtend();
        String[] msgIds = extend.split(",");
        List<String> msgIdList = new ArrayList<>();
        for (String msgId : msgIds) {
            if(StringUtils.isNoneBlank(msgId)){
                //非空msgId放入list中
                msgIdList.add(msgId);
            }
        }
        if (!CollectionUtils.isEmpty(msgIdList)) {
            chatDao.signMsg(msgIdList);
        }

    }
}
