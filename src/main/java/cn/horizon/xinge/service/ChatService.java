package cn.horizon.xinge.service;

import cn.horizon.xinge.netty.object.ChatContent;

/**
 * @author horizon
 * @create 2023/3/29 20:45
 **/
public interface ChatService {

    /**
     * 处理接受到的用户消息
     * @param content 消息内容
     */
    void receiveMsg(ChatContent content);

}
