package cn.horizon.xinge.common.exception;

import cn.horizon.xinge.common.api.ResultCode;
import lombok.Data;

/**
 * @author horizon
 * @create 2021-02-01 20:24
 **/
@Data
public class CustomException extends RuntimeException{

    private Integer code;

    public Integer getCode() {
        return code;
    }


    //ResultCode中未记录的错误类型
    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    //ResultCode中记录的错误类型
    public CustomException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code=" + code +
                ",message=" + this.getMessage() +
                '}';
    }
}
