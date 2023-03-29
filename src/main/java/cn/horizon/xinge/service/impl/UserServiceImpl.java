package cn.horizon.xinge.service.impl;

import cn.horizon.xinge.common.api.Result;
import cn.horizon.xinge.common.api.ResultCode;
import cn.horizon.xinge.constant.AuthConstants;
import cn.horizon.xinge.domain.User;
import cn.horizon.xinge.domain.vo.UserVo;
import cn.horizon.xinge.repository.UserDao;
import cn.horizon.xinge.service.UserService;
import cn.horizon.xinge.utils.JwtTokenUtil;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author horizon
 * @create 2023/3/24 22:24
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final LambdaQueryWrapper<Object> lambdaQueryWrapper = new LambdaQueryWrapper<>();

    @Override
    public Result<Object> login(UserVo user) {

        if (user.getEmail() == null && user.getUsername() == null) {
            return Result.failed(ResultCode.BAD_REQUEST); // 参数不合法
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //使用用户名登陆
        if (user.getUsername() != null) {
            queryWrapper.eq(User::getUsername, user.getUsername());
        }
        //使用邮箱登陆
        if (user.getEmail() != null) {
            queryWrapper.eq(User::getEmail, user.getEmail());
        }
        User userDto = userDao.selectOne(queryWrapper);
        if (userDto == null) {
            return Result.failed(ResultCode.USER_NOT_EXIST); // 用户不存在
        }
        if (!BCrypt.checkpw(user.getPassword(), userDto.getPassword())) {
            return Result.failed(ResultCode.WRONG_PASSWORD); // 密码错误
        }

        //TODO Netty端登陆

        Map<String, String> map = new HashMap<>();
        map.put("uid", userDto.getId().toString());
        map.put("username", userDto.getUsername());

        String token = jwtTokenUtil.getToken(map);
        map.clear();
        map.put("token", token);
        return Result.success(map);
    }

    @Override
    public Result<Object> register(UserVo userVo) {

        if (userVo.getUsername() == null || userVo.getEmail() == null || userVo.getPassword() == null) {
            return Result.failed(ResultCode.BAD_REQUEST); // 参数不合法
        }

        User userDto = new User();
        User userCheck;
        userCheck = getUserInfo("email", userVo.getEmail());
        if (userCheck != null) {
            return Result.failed(ResultCode.EMAIL_IS_EXIST);
        }
        userCheck = getUserInfo("username", userVo.getUsername());
        if (userCheck != null) {
            return Result.failed(ResultCode.USER_IS_EXIST);
        }
        String hashPw = BCrypt.hashpw(userVo.getPassword(), BCrypt.gensalt(10));
        BeanUtils.copyProperties(userVo, userDto);
        userDto.setPassword(hashPw);
        userDao.insert(userDto);


        return Result.success();
    }

    @Override
    public Result<Object> userInfo(String filterStr) {
        User userDto = null;
        String[] filters = {"id", "username", "email"};

        // Iterate through filters
        for (String filter : filters) {
            userDto = getUserInfo(filter, filterStr);
            if (userDto != null) {
                break;
            }
        }
        // Check if userDto is null
        if (userDto == null) {
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }

        // Set password to null
        userDto.setPassword(null);

        return Result.success(userDto);
    }

    @Override
    public Result<Object> userInfo(HttpServletRequest request) {
        String auth = request.getHeader(AuthConstants.AUTHENTICATION);
        String token = auth.replace(AuthConstants.TOKEN_PREFIX, "");

        Claim claim = jwtTokenUtil.getClaimByName(token, "uid");
        String uid = claim.asString();
        User userInfo = getUserInfo("id", uid);
        userInfo.setPassword(null);

        return Result.success(userInfo);
    }


    public User getUserInfo(String fieldName, String value) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(fieldName, value);
        return userDao.selectOne(queryWrapper);
    }

}
