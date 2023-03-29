package cn.horizon.xinge.service;

import cn.horizon.xinge.common.api.Result;
import cn.horizon.xinge.domain.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author horizon
 * @create 2023/3/24 22:22
 **/
public interface UserService {
    /**
     * 用户登陆
     * @param user 用户登陆信息
     * @return  {@link cn.horizon.xinge.common.api.Result}
     */
    Result<Object> login(UserVo user);

    /**
     * 用户注册
     * @param userVo 用户注册信息（至少包含用户名，邮箱，密码）
     * @return {@link cn.horizon.xinge.common.api.Result}
     */
    Result<Object> register(UserVo userVo);

    /**
     * 获取用户信息
     * @param filterStr 可以通过用户名 / 邮箱 / id获取用户信息
     * @return {@link cn.horizon.xinge.common.api.Result}
     */
    Result<Object> userInfo(String filterStr);

    /**
     * 获取当前登陆用户信息
     * @return  {@link cn.horizon.xinge.common.api.Result}
     */
    Result<Object> userInfo(HttpServletRequest request);

}
