package cn.horizon.xinge.common.netty;

/**
 * @author horizon
 * @create 2023/3/29 20:08
 **/
public enum ChatActionEnum {

    CONNECT(1, "第一次(或重连)初始化连接"),
    CHAT(2, "聊天消息"),
    SIGNED(3, "消息签收"),
    KEEPALIVE(4, "客户端保持心跳"),
    PULL_FRIEND(5, "拉取好友");
    public final Integer type;
    public final String content;

    ChatActionEnum(Integer type, String content){
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
}
