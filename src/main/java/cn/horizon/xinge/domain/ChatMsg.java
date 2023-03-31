package cn.horizon.xinge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 聊天消息对象
 * @author horizon
 * @create 2023/3/30 18:55
 **/
@Data
public class ChatMsg implements Serializable {


    @Serial
    private static final long serialVersionUID = 4314144268856218591L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("sender_user_id")
    private Integer sendUid;

    @TableField("accept_user_id")
    private Integer acceptUid;

    private String msg;

    /**
     * 是否已读
     */
    private Boolean signFlag;

    /**
     * 消息创建时间
     */
    private Timestamp createTime;



}
