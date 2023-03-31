package cn.horizon.xinge.netty.object;

import cn.horizon.xinge.common.netty.ChatActionEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author horizon
 * @create 2023/3/29 20:01
 **/
@Data
public class ChatContent implements Serializable {

    @Serial
    private static final long serialVersionUID = -8922654650215586619L;
    //动作类型,参考消息类型的枚举
    private ChatActionEnum action;
    //传递过来的消息
    private ChatObject data;
    //扩展字段
    private String extend;

}
