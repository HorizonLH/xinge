package cn.horizon.xinge.common.api;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一接口返回对象
 * @author horizon
 * @create 2022-06-21 21:28
 **/
@Getter
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示内容，如果接口出错，则存放异常信息
     */
    private String msg;

    /**
     * 返回数据体
     */
    private T data;

    public Result() {}

    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(ResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMsg(), data);
    }
    //--------------------Success----------------------
    /**
     * 成功响应，无响应数据
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, null);
    }

    /**
     * 成功，有响应数据
     * @param data 响应数据
     * @return Result
     * @param <T> 响应数据类型
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }

    //--------------------Failed----------------------

    /**
     * 失败，自定义失败消息，无数据
     * @param msg 响应失败消息
     * @return Result
     */
    public static <T> Result<T> failed(String msg) {
        return new Result<>(ResultCode.FAILED.getCode(), msg, null);
    }

    /**
     * @param code error code
     * @param msg 错误消息
     * @return Result
     */
    public static <T> Result<T> failed(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 失败，自定义失败消息，有数据
     * @param msg 响应失败消息
     * @param data 响应数据
     * @return Result
     * @param <T> 响应数据类型
     */
    public static <T> Result<T> failed(String msg, T data) {
        return new Result<>(ResultCode.FAILED.getCode(), msg, data);
    }

    /**
     * 失败，固定失败类型，无数据
     * @param errorCode 类型为{@code ResultCode}的失败消息和code
     * @return Result
     */
    public static <T> Result<T> failed(ResultCode errorCode) {
        return new Result<>(errorCode, null);
    }

    /**
     * 失败，固定失败类型，有数据
     * @param errorCode 类型为{@code ResultCode}的失败消息和code
     * @param data 响应数据
     * @return Result
     * @param <T> 响应数据类型
     */
    public static <T> Result<T> failed(ResultCode errorCode, T data) {
        return new Result<>(errorCode, data);
    }

    public static <T> Result<T> failed(ResultCode resultCode, String msg) {
        return new Result<>(resultCode.getCode(), msg, null);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
