package cn.horizon.xinge.domain.vo;

import cn.horizon.xinge.domain.User;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author horizon
 * @create 2023/3/27 21:28
 **/
@Data
public class UserVo extends User implements Serializable {


    @Serial
    private static final long serialVersionUID = -6673732953402223463L;

    private String token;

}
