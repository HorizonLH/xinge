package cn.horizon.xinge.common.netty;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * user Id和channel的关系类,用于存储对应的关联
 * @author horizon
 * @create 2023/3/29 20:24
 **/
public class UserChannelRel {

    //定义静态变量
    public static Map<String, Channel> manager = new HashMap<>();

    //定义存储方法
    public static void put(String senderId, Channel channel){
        manager.put(senderId,channel);
    }

    //定义获取方法
    public static Channel get(String senderId){
        //根据senderId获取对应的channel
        return manager.get(senderId);
    }

    //输出所有的user和channel信息
    public static void output(){
        for (Map.Entry<String, Channel> channelEntry : manager.entrySet()) {
            System.out.println("user: "+ channelEntry.getKey()+", channelId: "+channelEntry.getValue().id().asLongText());
        }
    }
}
