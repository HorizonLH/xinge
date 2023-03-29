package cn.horizon.xinge.netty.object;

import lombok.Data;

import java.io.Serializable;

/**
 * @author horizon
 * @create 2023/3/29 19:59
 **/
@Data
public class ChatObject implements Serializable {

    //发送者id
    private Integer senderId;
    //接收者id
    private Integer receiverId;
    //消息的主体
    private String msg;
    //消息的id,存于消息记录中,对应于数据库的消息的id
    private String msgId;

}
