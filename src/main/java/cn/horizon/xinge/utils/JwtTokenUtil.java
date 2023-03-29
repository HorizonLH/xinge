package cn.horizon.xinge.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author horizon
 * @create 2023/3/24 21:23
 **/
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expire;



    /**
     * 创建Token
     * @param map 前台传输的用户信息
     * @return Token
     */
    public String getToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();

        // 声明过期时间
        Calendar calendar = Calendar.getInstance();
        int day = Math.toIntExact(expire / 60 / 60 / 24);
        calendar.add(Calendar.DATE, day);
            Date expireTime = calendar.getTime();

        // Header默认加载，不写也行
        builder.withHeader(new HashMap<>());

        // Payload装载数据
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }
        builder.withExpiresAt(expireTime);

        // Signature配置，同时获取最终生成的Token

        return builder.sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证Token合法性，假如Token被修改，则抛出异常
     * @param token 待验证的Token
     */
    public void verifyToken(String token) {
        // 验证Token必须使用和创建一样的算法和私钥
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    /**
     * 通过载荷名字获取载荷的值
     * @param token 已验证的Token
     * @param name payload中的key
     * @return payload中的value
     */
    public Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }


}
