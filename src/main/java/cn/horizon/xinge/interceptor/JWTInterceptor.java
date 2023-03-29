package cn.horizon.xinge.interceptor;

import cn.horizon.xinge.annotation.NoTokenApi;
import cn.horizon.xinge.common.api.ResultCode;
import cn.horizon.xinge.common.exception.CustomException;
import cn.horizon.xinge.constant.AuthConstants;
import cn.horizon.xinge.utils.JwtTokenUtil;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * @author horizon
 * @create 2023/3/24 22:02
 **/
@Configuration
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String auth = request.getHeader(AuthConstants.AUTHENTICATION);
        if (auth == null) {
            throw new CustomException(ResultCode.FAILED);
        }
        String token = auth.replace(AuthConstants.TOKEN_PREFIX, "");

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        //检查是否有NoTokenApi注释，有则跳过认证
        if (method.isAnnotationPresent(NoTokenApi.class)) {
            return true;
        }
        try {
            // 验证token
            jwtTokenUtil.verifyToken(token);
            // 放行请求
            return true;
        } catch (SignatureVerificationException e1) {
            throw new CustomException(ResultCode.INVALID_TOKEN_SIGNATURE);
        } catch (TokenExpiredException e2) {
            throw new CustomException(ResultCode.TOKEN_EXPIRED_ERROR);
        } catch (AlgorithmMismatchException e3) {
            throw new CustomException(ResultCode.MISS_TOKEN_CLAIM);
        } catch (InvalidClaimException e4) {
            throw new CustomException(ResultCode.MISMATCH_TOKEN_CLAIM);
        }


    }
}
