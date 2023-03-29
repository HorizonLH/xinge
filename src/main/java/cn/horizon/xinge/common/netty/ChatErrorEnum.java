package cn.horizon.xinge.common.netty;

/**
 * @author horizon
 * @create 2023/3/29 21:18
 **/
public enum ChatErrorEnum {

    USER_NOT_CONNECT(1, "用户{0}未登陆");

    private final int code;
    private final String msg;

    private ChatErrorEnum(int code, String msg) {
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
