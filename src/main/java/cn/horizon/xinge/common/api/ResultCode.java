package cn.horizon.xinge.common.api;

/**
 * 统一接口状态码
 * @author horizon
 * @create 2022-06-21 21:32
 **/
public enum ResultCode {

    /**
     * api状态码管理
     */
    SUCCESS(0, "请求成功"),
    FAILED(10001, "操作失败"),
    BAD_REQUEST(10003, "参数不合法"),
    METHOD_NOT_ALLOWED(10004, "方法不允许"),
    TOKEN_EXPIRED_ERROR(10002, "token已超时"),
    INVALID_TOKEN_SIGNATURE(10005, "token签名无效"),
    MISS_TOKEN_CLAIM(10006, "token缺少声明"),
    MISMATCH_TOKEN_CLAIM(10007, "token声明不匹配"),

    /**
     * 登录/注册
     */
    USER_NOT_EXIST(20001, "用户不存在"),
    REGISTER_FAILED(21001, "注册失败"),
    WRONG_CODE(21002, "验证码错误"),
    WRONG_PASSWORD(21003, "密码错误"),

    USER_IS_EXIST(21004, "用户名已存在"),
    EMAIL_IS_EXIST(21005, "邮箱已被注册"),



    NONE(99999, "无");

    private final int code;
    private final String msg;

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
