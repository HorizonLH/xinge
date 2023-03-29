package cn.horizon.xinge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author horizon
 * @create 2022-06-23 21:24
 **/
@Data
public class User {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "格式不正确")
    private String email;

    /**
     * 头像
     */
    private String avatar;

//    /**
//     * 在线状态
//     */
//    private UserStatus status;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 电话
     */
    private String phoneNumber;

    /**
     * 生日
     */
    private Date birth;


    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 修改时间
     */
    private Timestamp updatedAt;


}
